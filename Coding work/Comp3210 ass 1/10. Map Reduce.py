# PT1

#Creates a list of all tweets dates
from pymongo import MongoClient # import package

client= MongoClient("localhost:27017") # connect to database
db = client.Prac1 # sets up database
query=db.Tweets.find({})# finds tweets

for q in query:# loops through tweets
    if(q["actor"]["objectType"] == "person"): # looking for those that are person
        date = q["actor"]["postedTime"] # saves date & time as a string
        print(date[0:10]) # prints first 10 characters of string
        
# PT 2

from mrjob.job import MRJob
class MRFrequencyCount(MRJob):
 def mapper(self, _, line):
     yield (line, 1) # count num of characters
 def reducer(self, key, values):
     yield key, sum(values)
     
if __name__ == '__main__':
    MRFrequencyCount.run() # main program to call/run MRWordFrequencyCount
    
# PT 3

from mrjob.job import MRJob
from mrjob.step import MRStep
class MRsort(MRJob):
    
 def steps(self):
     return [MRStep(
         mapper = self.mapper_split,
         reducer = self.reducer_t
         ),
         MRStep(reducer = self.sort)]
 def mapper_split(self, _, line):
     count = int(line[16:200])
     key = line[1:11]
     yield key, count 
     
 def reducer_t(self, key, values):
     yield None, (sum(values), key)

 def sort(self, _, bigCounts):
    for count, key in sorted(bigCounts, reverse=True):
        yield (key, count)
        
if __name__ == '__main__':
    MRsort.run()
