CONTENTS OF REPO: This repository contains the code I wrote for an Independent Study project I did while completing my Master of Science in CS.
The project focused on classifying Yelp reviews as either "useful" or "not useful".  A more in depth analysis can be found in report.pdf.

These Java applications were not meant to be run all at once, but instead were mostly used to process the data set and generate a Feature table
to be used in a SVM.  

The whole process of classifying was a combination of programs and tools.  The order that the programs should be run in, is as follows:

* ReviewProcessor
* POS Tagging--with Stanford Core NLP
* XMLParser
* FeaturesLoader
* Classification--with RapidMiner
* TestSetProcessor
* TestSetFeatureLoader
* Prediction--with RapidMiner
	
I will be continuously updating/improving the code.
