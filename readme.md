# Hierarchy App

### Review
Application provide ability to create a company structure tree and managing its components
like departments, locations and teams.
You can add a connection between teams to set coworkers and manage them.

### Technology used
 - Java 8
 - SpringBoot 2.0
 - JUnit
 - Maven, log4j

### Build app 
```
mvn package
```

### Run Tests
```
mvn test
```

### REST API

```
 
POST  /company/create   --- create a tree (JSON as a request body)
DELETE  company/delete   --- delete a structure
 
POST  company/hierarchy/add   --- add a object to the tree
              @RequestParam("type") decides what type of the object you add
              @RequestBody HierarchyDetails specify where to create object
              
DELETE company/hierarchy/remove   --- remove object from the tree   
              @RequestParam("type") decides what type of the object you remove
              @RequestBody HierarchyDetails specify from where you remove object
 
GET /company/hierarchy/get   --- get specific object from the tree
              @RequestParam("type") decides what kind of object will be returned
              @RequestParam department/location/team specify from where object should be returned
              
GET /company/hierarchy/get/all   --- get list of object names from selected tree level (by type)
              @RequestParam("type") decides which object's names will be returned
 
 
 
***** COWORKERS  *****

 
POST /coworker/add   --- add coworker to the team (return a map with the connections)
 
DELETE  /coworker/remove/{name}   --- remove a coworker by its name
 
GET  /coworker/get/all   --- return a map of the connected teams
 
GET  /team/get/coworker/{name}   --- return coworkers of the team by its name
```

### Design overview
Application is based on the CompanyManager which manages of the company structure.
There are department/location/team managers which implements ObjectManager interface. 
Such composition allows CompanyManager to switch between those managers depending on the sent object's type.
All information needed to create/remove or get object from hierarchy are providing by HierarchyDetails class.


#### Some examples of requests:
##### creating a tree structure
```
		[
          {
            "name": "marketing",
            "locations": [
                      {
                        "name": "Krakow",
                        "teams": [
                                  {
                                    "name": "teamMK0"
                                  },
                                  {
                                    "name": "teamMK1"
                                  },
                                  {
                                    "name": "teamMK2"
                                  }
                        ]
                      },
                      {
                        "name": "Warszawa",
                        "teams": [
                                  {
                                    "name": "teamMW3"
                                  }
                        ]
                      }
            ]
          },
          {
            "name": "IT",
            "locations": [
                      {
                        "name": "Krakow",
                        "teams": [
                                  {
                                    "name": "teamITK4"
                                  },
                                  {
                                    "name": "teamITK5"
                                  },
                                  {
                                    "name": "teamITK6"
                                  }
                        ]
                      },
                      {
                        "name": "New York",
                        "teams": [
                                  {
                                    "name": "teamITNY7"
                                  },
                                  {
                                    "name": "teamITNY8"
                                  },
                                  {
                                    "name": "teamITNY9"
                                  }
                        ]
                      }
            ]
          },
          {
            "name": "HR",
            "locations": [
                      {
                        "name": "Krakow",
                        "teams": [
                                  {
                                    "name": "teamHR10"
                                  },
                                  {
                                    "name": "teamHR11"
                                  },
                                  {
                                    "name": "teamHR12"
                                  },
                                  {
                                    "name": "teamHR13"
                                  }
                        ]
                      },
                      {
                        "name": "Warszaawa",
                        "teams": [
                                  {
                                    "name": "teamHRW14"
                                  },
                                  {
                                    "name": "teamHRW15"
                                  },
                                  {
                                    "name": "teamHRW16"
                                  },
                                  {
                                    "name": "teamHRW17"
                                  }
                        ]
                      }
            ]
          },
          {
            "name": "Customer Service",
            "locations": [
                      {
                        "name": "Krakow",
                        "teams": [
                                  {
                                    "name": "teamHRK18"
                                  },
                                  {
                                    "name": "teamHRK19"
                                  },
                                  {
                                    "name": "teamHRK20"
                                  },
                                  {
                                    "name": "teamHRK21"
                                  }
                        ]
                      }
            ]
          }
        ]
```


##### creating connection between teams

```
{
    "parentId": 2,
    "cooworkersIds": [6, 11]
}
```


###### returns connections between all given teams (see below):
```
{
    "teamHR11": [
        {
            "id": 6,
            "name": "teamITK6"
        },
        {
            "id": 2,
            "name": "teamMK2"
        }
    ],
    "teamMK2": [
        {
            "id": 6,
            "name": "teamITK6"
        },
        {
            "id": 11,
            "name": "teamHR11"
        }
    ],
    "teamITK6": [
        {
            "id": 11,
            "name": "teamHR11"
        },
        {
            "id": 2,
            "name": "teamMK2"
        }
    ]
}

```


###### returns list of teams in a given locationName:
```
[
    {
        "id": 4,
        "name": "teamITK4"
    },
    {
        "id": 5,
        "name": "teamITK5"
    }
]
```


###### returns list of department names:
http://localhost:8080/company/hierarchy/get/all?type=department
```
[
    "marketing",
    "IT",
    "HR",
    "Customer Service"
]
```

###### add department to the tree:
http://localhost:8080/company/hierarchy/add?type=department
```
{
    "departmentName": "newDepartment"
}
```


###### add location to the tree (to newDepartment):
http://localhost:8080/company/hierarchy/get/all?type=location
```
{
    "departmentName": "newDepartment",
    "locationName": "newLocation"
}
```

###### add team to the tree (to newLocation in newDepartment):
http://localhost:8080/company/hierarchy/get/all?type=team
```
{
    "departmentName": "newDepartment",
    "locationName": "newLocation",
    "teamName": "newTeam"
}
```


###### remove team to the tree (from newLocation in newDepartment):
http://localhost:8080/company/hierarchy/remove?type=team
```
{
    "departmentName": "newDepartment",
    "locationName": "newLocation",
    "teamName": "newTeam"
}
```



### TESTS

Tests cover logic of classes:
- CompanyManager
- CooperationService
- DepartmentManager
- LocationManager
- TeamManager

 
  

### Possible improvements  
- I have to learn writing acceptance tests and test API to complete this app.

###### Note
I changed a structure of the application. Now CompanyManager dynamically switches between object's managers.
I divide one huge controller for the smaller controllers.
Main defect is that app doesn't have acceptance test.I have to learn how to write them.