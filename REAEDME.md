# Claire Lee - Neighborhood Guide App
___
## 1. Overview

 This neighborhood guide app has 5 main features. You can search, review, rate, favorite a place and can see the search results much quicker by clicking the quick search buttons too. Everytime you launch the app, you can see random place suggestions and you can even search places by neighbor too. You can refine your search by the filter to get some specific results.  

___
## 2. Features
 This app includes these features : 
 
- Basic search - You can search any places based on 5 criteria (name, address, neighbor, type, keyword). Especially, this keyword criteria let you find a place more easily. You can just type 'chinese' and find some awesome chinese restaurants!

- Quick search - You can browse the result based on some specific keyword. When you click one of the nine buttons on the main page, you will be directly linked to the related result without typing query. You can just find places great for breakfas, lunch, dinner, or fun activity, and more. 

- Filter - You can refine your search by clicking the filter button. The filter dialog would be launched. You can filter places by price, wifi, and credit card availability. The filter setting stays even after re launching the filter dialog. 

- Review - You can write some review about the place and rate it at the same time. It would be automatically shown on the detail screen and the number of votes and rating would be also updated. You can just rate a place without leaving any review. 

- Favorite - You can bookmark a place by clicking the favorite button. Favorited items will be shown in the favorite item list page. 

- Random suggetions : You can see different places everytime you resume the main screen. It will give you some random suggestions. It is directly linked to the main page. 

___
## 3. Screenshots
- MainActivity
![alt-text](https://github.com/WasabiMayo/Project-2/blob/master/Screenshots/projectTwo_main.png?raw=true)

- ResultActivity
![alt-text](https://github.com/WasabiMayo/Project-2/blob/master/Screenshots/projectTwo_result.png?raw=true)

- DetailActivity
![alt_text](https://github.com/WasabiMayo/Project-2/blob/master/Screenshots/projectTwo_detail.png?raw=true)

- CommentActivity
![alt-text](https://github.com/WasabiMayo/Project-2/blob/master/Screenshots/projectTwo_review.png?raw=true)

- RatingDialog
![alt-text](https://github.com/WasabiMayo/Project-2/blob/master/Screenshots/projectTwo_rating.png?raw=true)

- FilterDialog
![alt-text](https://github.com/WasabiMayo/Project-2/blob/master/Screenshots/projectTwo_filter.png?raw=true)


___
## 4. Things that I'm proud of 
- Clean UI design
- Review system and filter system. 
- I wanted the whole layout of the detail activity to be scrollable, not only the listview. So I just inflated the detail layout and put that view as the header of the listview.  

___
## 5. Things to fix
- Layout looks different on larger devices. It runs well on nexus and my galaxy, but it doesn't look exactly same on other devices. 
- Counldn't store rating as double values. It only accepts integers. How can I store decimals in the database? 


