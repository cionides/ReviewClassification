__CREATED BY:__ Christina Ionides

__TO RUN:__ 
**Note-The run command is what I used for my machine, but may need to be
adjusted depending on available ram, etc.

* Open a terminal/command line window
* Navigate to the project directory
* java -d64 -Xms512m -Xmx4g -jar ReviewProcessor.jar

__INPUT FILES[Already In the Distribution Folder]:__
_ yelp_training_set/
⁃ yelp_training_set_business.json
⁃ yelp_training_set_review.json
⁃ yelp_training_set_user.json
- batch_files/
⁃ Empty before running*

__GENERATED/OUTPUT FILES:__
* user_object.txt
* A file of all user objects from training data
* review_object.txt
* A file of all review object from training data
* business_object.txt
* A file of all business objects from training data
* review_content.txt
* The text from all the selected review objects
* uvMap.txt
* The mapping of Useful Vote counts and the number of reviews with
that count
* batch_files/
* contains 500 txt files of the format: "review_partN.txt"
* These files are later used as input to the stanford core nlp tool, and
500 xml files of the tagged text are generated.
