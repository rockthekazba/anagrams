README

Ibotta Anagram Project
This project is an assignment from Ibotta to create a series of services that searches and returns various functions regarding anagrams. 

Accessing Code and Running Project
The project is built as a java jar project and is currently deployed on AWS EC2 instance. The AWS instance can be accessed over http at 
http://ec2-52-14-18-78.us-east-2.compute.amazonaws.com:9000/anagram/
Locally code can be built as jar and executed through command line by running java -jar [name of jar file]. The application runs on port 9000.

The code based is accessible via repository on GitHub and can be found at: https://github.com/rockthekazba/anagrams

The project is built as a Spring Boot project using Maven and comprises a several RESTful APIs as indicated below. Java 8 runtime and jdk is utilized.

API endpoints:
POST: http://ec2-52-14-18-78.us-east-2.compute.amazonaws.com:9000/anagram/createcorpus
Summary:Creates a wordlist (corpus) used by user to return anagrams
Sample json: { "words": ["read", "dear", "dare"] }
Returns: HTTP 200 on success as well as list of words just created
Returns: HEADER variable: sessionid - Session Id used for all subsequent calls 

GET:http://ec2-52-14-18-78.us-east-2.compute.amazonaws.com:9000/anagram/
Required request HEADER variable: sessionid - Returned from the createcorpus service
Summary: Returns all words in the corpus
Returns: HTTP 200 on success as well as list of words from corpus

GET:http://ec2-52-14-18-78.us-east-2.compute.amazonaws.com:9000/anagram/{word}
Summary: Returns any anagrams for a word passed in the url. Takes two optional parameters:
	limit = max number of anagrams returned for the word
	excludeProper = if true then will not return any anagrams that are proper nouns. Proper nouns are defined as words that start with an upper case letter
Required request HEADER variable: sessionid - Returned from the createcorpus service
Returns: HTTP 200 on success as well as list of anagrams for word
	
DELETE: http://ec2-52-14-18-78.us-east-2.compute.amazonaws.com:9000/anagram/
Summary: Deletes all words from the corpus
Required request HEADER variable: sessionid - Returned from the createcorpus service
Returns: HTTP 204

DELETE: http://ec2-52-14-18-78.us-east-2.compute.amazonaws.com:9000/anagram/{word}
Summary: Deletes specific word from corpus
Required request HEADER variable: sessionid - Returned from the createcorpus service
Returns: HTTP 204

GET: http://ec2-52-14-18-78.us-east-2.compute.amazonaws.com:9000/anagram/corpusstats
Summary: Returns statistics on corpus
Required request HEADER variable: sessionid - Returned from the createcorpus service
Returns: HTTP 200: word count, minimum word length, maximum word length, median word length, average word length

GET: http://ec2-52-14-18-78.us-east-2.compute.amazonaws.com:9000/anagram/mostanagrams
Summary: Finds words in corpus that have the most anagrams
Required request HEADER variable: sessionid - Returned from the createcorpus service
Returns: HTTP 200 returns lists of words with the most anagrams and their words

POST: http://ec2-52-14-18-78.us-east-2.compute.amazonaws.com:9000/anagram/anagramcomparecheck
Summary: Takes a list of words and checks if those words are anagrams of each other
Sample Json: { "words": ["read", "dear", "dare"] }
Required request HEADER variable: sessionid - Returned from the createcorpus service
Returns: HTTP 200 - List of lists of words that are anagrams of each other, empty if none

GET: http://ec2-52-14-18-78.us-east-2.compute.amazonaws.com:9000/anagram/anagramgroup
Summary: Returns a list of word groups that have a number of anagrams of specific size. Takes required parameter
	groupsize=[integer] - Parameter indicating words with only as many anagrams specified 
Required request HEADER variable: sessionid - Returned from the createcorpus service
Returns: HTTP 200 - List of words and anagrams with groupsize indicated
	
	
Storage:
Storage of the word list is stored in memory. A file "dictionary.txt" which is a list of all english words and used to find anagrams for specific words is stored in memory on startup.




