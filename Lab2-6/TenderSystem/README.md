### Tender System
###### Routes of the applications
- "/login" page with login form
- "/profile" page with user offers
- "/tenders" page with active tenders and search
- "/tenders/create" page with creation form
- "/tenders/{id}" page with the tender found by id, make offer form
- "/tenders/{id}/offers" page with the tender offers

###### Controllers
- IndexController - always redirect to tenders
- TenderController - tenders and offers controller
- UserController - user profile page

The application uses Spring Security for authorization
