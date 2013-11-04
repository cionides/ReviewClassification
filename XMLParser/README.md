__TO RUN:__

Note-The run command is what I used for my machine, but may need to be
adjusted depending on available ram, etc.
* Open a terminal/command line window
* Navigate to the dist directory
* java -d64 -Xms512m -Xmx4g -jar XMLParser.jar

__INPUT FILES[Already Included in dist folder]__
* posTags.txt⁃ This is a list of part of speech tags used in the stanford core nlp
program that need to be stripped from the xml output
* stopwords.txt⁃ This is a list of common stop words read into the program to be
parsed out of potential noun phrases
* xml_output⁃ Contains the xml formatted tagged text files from the stanford core nlp process. They are in the format: "review_partN.txt.xml"

__GENERATED/OUTPUT FILES__
* nounPhraseMap.txt⁃ This file gives a sorted list of noun phrases and their frequency.  It is from this file that I selected the top 25 noun phrases to be used
as features in the review features table
* tagged_review_sentences.txt⁃ This is an intermediary file that is created during the processing. It contains all of the noun phrases extracted from the xml_output files.
This is before they have been truly processed.
