__CREATED BY:__ Christina Ionides

__TO RUN:__
**Note-The run command is what I used for my machine, but may need to be
adjusted depending on available ram, etc.
* Open a terminal/command line window
* Navigate to the dist directory
* java -d64 -Xms512m -Xmx4g -jar FeaturesLoader.jar

__INPUT FILES[Already Included in dist folder]__
* top_np.txt- This file contains the top 25 noun phrases selected from the
nounPhraseMap generated in XMLParser. These words are read
in to the program and used as features for each review instance.
* review_object.txt- This file is taken from the ReviewProcessor.java program and holds
the 87,359 review objects we are classifying.
*stopwords.txt⁃ This is the same util file found in the other programs that contains a
list of stop words to be read in to the program and is used to help
determine the length of a review without stop words.

__GENERATED/OUTPUT FILES:__
*review_features.txt⁃ This is the file that will be read into RapidMiner. It is a table of the
87,359 review instances and their features. Each row represents a review, and each column is a feature. The
last column is the "label" where a 1 means "useful" and a 0 means "not useful"
