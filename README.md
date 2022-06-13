# Mountain-App
Capstone Bangkit 2022

**Backgrounder**
<br/>
Indonesia has many beautiful mountains. However, what has always been a problem is the lack of information about mountains in Indonesia. In addition, many still have difficulty identifying and finding information about mountains in some areas. Based on West Java Open Data, the number of tourists during the pandemic dropped dramatically. With the loosening of regulations from the government, and the opportunity to revive the tourism industry, we have the following idea, which is a one-stop mountain tourism application specifically dealing with mountain tourism which is very popular among domestic and international tourists. 

We want to solve this problem because as we know, Indonesia has many mountains that can be good tourist attractions for domestic and foreign tourists. We offer a one stop mountain traveling application by providing information related to mountains in Indonesia. In addition to this one stop mountain traveling application, we added an image backup search feature that makes it easy to identify using images from their device or directly using the camera on the device. By using this one stop mountain traveling application, it is hoped that it can revive the tourism industry in Indonesia and become a liaison for service providers for the needs of mountain tourists in order to improve the creative economy.

Tech stack:

**Mobile Developer**
<br/>
Implemented the UI and deployment of TensorFlow lite presented in the android application. We created Low-Fidelity first before creating UI/UX Layout to get an overview of the application quickly. Next, we created the UI/UX by implementing the UX principles we learned earlier. Then, we implemented the UI/UX to the code in android studio. We Also consumed Rest API from CC. We use a version control system while developing our application. We successfully bridge the gap of each path by implementing in mobile application, so the core function in our application can run smoothly, that it scanning mountains for detailed information about mountains. The feature in our application are Homepage, feeds, favorite, profile, detail mount, scan from gallery or camera, language, splashscreen, login, and signup.

**Machine Learning Developer**
<br/>
For Machine Learning, First we collect data in the form of images in any size by manual web scraping. We collected 5 classes of mountain with each class containing 60 images so the total image is 300. Then We split it into the train-validation set. Next we built the model using Tensorflow with 3 Convolutional Layers and 2 Hidden Layers with ReLU activation function and finally for the output layer We used softmax activation with 5 neurons indicating 5 classes of classifications. For the model accuracy we got 81% , 80% , and 78% for training, validation, and test set respectively. For the last step We saved the model in tensorflow lite format and deploy it. 

**Cloud Computing**
<br/>
Implemented REST API using MySQL, Express, and NodeJS stack. First we created a simple CRUD API to be consumed by MD team. With further requests from MD team, we have expanded the API to have various functions, which dealt with users, mountain, mountain_detail, mountain_review, feeds data. Besides backend service which is REST API, we also handled services which are related to Google Cloud Platform. We used App Engine to deploy the API, used Cloud SQL to store structured data, used Cloud Storage to store images in bucket. We also do simple monitoring on billing incurred from the project. To ensure the security of our project, we implemented Cloud IAM to prevent unauthorized access. 
