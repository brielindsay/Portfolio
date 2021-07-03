import time
import sys

""" Libary utilised to create, store and query a RTree
        from https://rtree.readthedocs.io/en/latest/index.html
        Rtree contains two classes, Index & Property - this code only uses Index 
        Index uses the defult construction while property allows the user to manipulate the constructer
        functions used: 
            Index(): used to initialise the RTree
            insert(id,(cord)): used to add items into the RTree automatically
            count(corinates of query boundaries): used to count amount of points within the RTree
"""
from rtree import index 


call = str(sys.argv)    # returns the command 
argument = []           # new array used to store the command as a an array
argument = call.split() # adds command to array
dataPoints = str(argument[len(argument)-2])     # creates a string of the file name of the data file
dataPoints = dataPoints[1:len(dataPoints)-2]    # removes ' from data file name
queryData = str(argument[len(argument)-1])      # creates a string of the file name of the query file
queryData = queryData[1:len(queryData)-2]       # removes ' from query file name


""" TEST IF FILE IS AVAILABLE"""
   
def testDocs():   
    file = None                                     # create variable to store open file code
    while file == None:
        try:
            file = open(dataPoints,'r')             # attemps to open data file
        except:
            print('please use correct data file name')   # if file name is incorrect or doesnt exist print line
            sys.exit()                              # exit code
    
    file = None                                     # create variable to store open file code
    while file == None:
        try:
            file = open(queryData,'r')              # attemps to open data file
        except:
            print('please use correct query file name')  # if file name is incorrect or doesnt exist print line
            sys.exit()                              # exit code


"""Main code"""

def main():
    
    """read the data points & queries"""

    with open('query_result.txt', 'w') as writer:   # Opens result file to enable writing
        points=[]                                   # Array to store points
        n = 0                                       # n stores count of data point - later used to create progress bar
        with open(dataPoints,'r') as dataset:       # Opens data points file
            for data in dataset.readlines():        # Loops through the lines for data points file
                data = data.split()                 # Splits the data on spaces
                points.append({                     # Adds data to points
                    'id':int(data[0]),              # Stores ID
                    'x' :int(data[1]),              # Stores x value of data points
                    'y' :int(data[2])})             # Stores y value of data points
                n = n +1                            # Adds one to the count of points
    
    
        # Read queries
        
        query=[]                                    # Array to store queries in an array                                
        with open(queryData,'r') as queries:        # Opens query data file
            for q in queries.readlines():           # loops through the lines
                data = q.split(" ",4)               # splits the data
                query.append({                      # adds data to query
                    'x1':int(data[0]),              # stores x value of 1st query bounds
                    'x2':int(data[1]),              # stores x value of 2nd query bounds
                    'y1':int(data[2]),              # stores y value of 1st query bounds
                    'y2':int(data[3])})             # stores y value of 2nd query bounds
                
                
        # Build RTree

        print("Building the R-Tree:")                         # Prints to console when starting the creation of RTree
        count = 1                                             # variable used to update progress of RTree build
        tree = index.Index()                                  # creats an empty RTree
        for point in points:                                  # loop to insert data points from the root one by one 
            tree.insert(point['id'], (point['x'],point['y'])) # inserts point into the RTree utilising the RTREE libary insert function
            bar(n,count)                                      # update to number of points
            count=count+1                                     # increase count
         
            
        # Sequential-Scan Based Method
        
        writer.write('Queries Sequentially')             # adds sequentially sub heading to write file
        querycopy = query                                # Copies queries - avoids potential altering of original query files
        start1 = time.perf_counter()                     # saves time - used to calculate sequential times
        for q in querycopy:                              # loops through all queries
            result = 0                                   # variable to save the amount of points that are within a query
            for point in points:                         # loops through all points
                if (q['x1']<=point['x']<=q['x2']):       # check if point x is within the query bounds
                    if(q['y1']<=point['y']<=q['y2']):    # check if point y is within the query bounds
                        result = result + 1              # if the condition above is solved adds one to the count of points
            writer.write("\n"+ str(result))              # writes amount of points found in each query
        end1 = time.perf_counter()                       # saves time - used to calculate sequential times
        timeBetween1 = end1-start1                       # calculates time for Sequential-Scan Based Method
        
                
        # queries RTree 
        writer.write("\n"+'Queriers RTree based method')                                       # adds RTree sub heading to write file
        start3 = time.perf_counter()                                              # saves time - used to calculate RTree times
        for q in query:                                                           # loops through all queries
           writer.write("\n"+ str(tree.count((q['x1'],q['y1'],q['x2'],q['y2'])))) # writes amount of points found in each query
        end3 = time.perf_counter()                                                # saves time - used to calculate RTree times
        timeBetween3 = end3-start3                                                # calculates time for Sequential-Scan Based Method


       # Prints times to console
       
        time1 = fs(timeBetween1)              # removes scientific notation from float to string conversion of sequential query time
        time3 = fs(timeBetween3)              # removes scientific notation from float to string conversion of RTree query time
        avgTime1 = fs(timeBetween1/count)     # removes scientific notation from float to string conversion of average sequential query time
        avgTime3 = fs(timeBetween3/count)     # removes scientific notation from float to string conversion of average RTree query time
        fast = fs(timeBetween1/timeBetween3)  # removes scientific notation from float to string conversion of RTree faster calculation
        
        print('\n')                                                                 # adds new line to console
        print('total time for sequential queries: '+time1)                          # prints time for sequential queries
        print('average time for sequential query: '+avgTime1)                       # prints average time for sequential queries
        print('\n')                                                                 # adds new line to console
        print('total time for R-Tree query: '+time3)                                # prints time for RTree queries
        print('average time for R-Tree query: '+avgTime3)                       # prints average time for RTree queries
        print('\n')                                                                 # adds new line to console
        print('R-tree is '+ fast+ ' times faster than sequential query' + '\n')     # prints RTree faster calculation
        writer.flush()                                                              # pushes changes to write file
        writer.close()                                                              # closes write file
   
            
""" function for progress bar
        Based on discusion from https://stackoverflow.com/a/15860757/1391441
        specifically EusouBrasileiro, Brian Khuu & Gabriel's responses
"""

def bar(total, progress):                       # defines bar function
    barLength, status = 20, ""                  # creates variable to hold barlength and status
    progress = float(progress) / float(total)   # calculates progress of bar
    if progress >= 1.:                          # if progress is less than than 1
        progress, status = 1, "\r\n"                # progress is updates to 1
    block = int(round(barLength * progress))    # calculates where progress is
    text = "\r[{}] {:.0f}% {}".format(          # updates text with variables above
        "#" * block + "-" * (barLength - block), round(progress * 100, 0),
        status)
    sys.stdout.write(text)                      # writes text specified above


""" function for converting float to string - removes scientific notation
        Based on Karin's response 
        on https://stackoverflow.com/questions/38847690/convert-float-to-string-in-positional-format-without-scientific-notation-and-fa?fbclid=IwAR0UbJcus5bOwjH3KT70tylNPaxrvo7AbLS8YArrKBzMPCTXBNKCWhiCK0o
"""

def fs(f):                                                  # declares fs function
    float_string = repr(f)                                  # creates variable to hold float being converted
    if 'e' in float_string:                                 # detect if scientific notation
        digits, exp = float_string.split('e')                   # splits scientific notation on e
        digits = digits.replace('.', '').replace('-', '')       # removes characters
        exp = int(exp)                                          # saves int from split above
        zero_padding = '0' * (abs(int(exp)) - 1)                # minus 1 for decimal point in the sci notation
        sign = '-' if f < 0 else ''                             # adds padding if neccessary
        if exp > 0:
            float_string = '{}{}{}.0'.format(sign, digits, zero_padding)
        else:
            float_string = '{}0.{}{}'.format(sign, zero_padding, digits)
    return float_string     # returns float as a string
    
if __name__ == '__main__':  # stops code running on accident if true runs code
    testDocs()              # tests if files called exsist within directory
    main()                  # runs code

