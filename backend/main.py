from fastapi import FastAPI, Response, status
from pydantic import BaseModel
import fastapi_problem_details as problem

class Person(BaseModel):
    id: int
    userId: str | None = None
    name: str | None = None
    birthYear: int
    birthMonth: int
    birthDayOfMonth: int
    remarks: str | None = None
    pictureUrl: str | None = None
    age: int
# Age should be read-only (check that later)

app = FastAPI()
problem.init_app(app)

@app.get("/api/Persons", status_code=200)
async def get_persons() -> list[Person]:
    return persons_list
# Add request body validation
@app.post("/api/Persons", status_code=201)
async def post_person(person: Person, response: Response):
    if validate_person(person):
        add_person(person)
        return{"message":"Person added successfuly"}
    response.status_code = status.HTTP_400_BAD_REQUEST
    return{"message":"There was a problem adding a person"}

@app.get("/api/Persons/{id}", status_code=200)
async def get_person(id: int, response: Response):
    person = find_person_by_id(id)
    if isinstance(person, Person):
        return person
    response.status_code = status.HTTP_404_NOT_FOUND
    return{"message":"There is no person like that"}

@app.put("/api/Persons/{id}", status_code=200)
async def update_person(person: Person, id: int, response: Response):
    current_person = find_person_by_id(id)
    if isinstance(current_person, Person):
        if validate_update_person(current_person, person):
            return{"message":"Update was successfull"} # add actual updating
        response.status_code = status.HTTP_400_BAD_REQUEST
        return{"message":"There was a problem updating a person"}
    response.status_code = status.HTTP_404_NOT_FOUND
    return{"message":"There is no person like that"}

@app.delete("/api/Persons/{id}", status_code=200)
async def delete_person(id: int, response: Response):
    if remove_person(id):
        return{"message":"Person deleted successfuly"}
    response.status_code = status.HTTP_404_NOT_FOUND
    return{"message":"There was a problem deleting a person"}


#-----------------
persons_list = [
    Person(id=1, userId="CLICK THIS", name="IMPORTANT INFORMATION", birthYear = 2000, birthMonth= 1, birthDayOfMonth= 1, remarks = "We are currently working on the back-end part of our application, therefore it is not accessible", age=15),
    Person(id=2, userId="abc123", name="John Doe", birthYear = 2000, birthMonth= 1, birthDayOfMonth= 1, remarks = "We are currently working on the back-end part of our application, therefore it is not accessible", age=15),
    Person(id=3, userId="CLICK THIS", name="Jane Doe", birthYear = 2000, birthMonth= 1, birthDayOfMonth= 1, remarks = "We are currently working on the back-end part of our application, therefore it is not accessible", age=15)
]

def add_person(new_person: Person):
    new_person.id=persons_list[-1].id + 1 # Will cause issues when the list is empty OR when items do get removed there will be gaps
    persons_list.append(new_person)

def remove_person(id:int):
    for item in persons_list:
        if item.id == id:
            persons_list.remove(item)
            return True
    return False

def find_person_by_id(id: int):
    for item in persons_list:
        if item.id == id:
            return item
    return False

def validate_person(person: Person):
    if 1+1:
        return True # define validation logic. But its also a good idea to use validation directly on api request

def validate_update_person(current_person: Person, updated_person: Person):
    1+1 # define logic here