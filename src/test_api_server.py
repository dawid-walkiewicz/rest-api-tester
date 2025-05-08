from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()

@app.get("/items")
def get_items_by_id():
    items = [
        {"id": 1, "name": f"Item 1"},
        {"id": 2, "name": f"Item 2"},
        {"id": 3, "name": f"Item 3"}
    ]
    return items

@app.get("/item/id/")
def get_fake_item():

    return {
        "id": "1",
        "name": "Test Item"
    }

@app.get("/item/id/{id}")
def get_item_by_id(id: int):
    return {
        "id": id,
        "name": f"Item with id {id}"
    }