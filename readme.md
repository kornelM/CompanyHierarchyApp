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
/company/hierarchy...
 
GET  /create   --- create a tree
DELETE  /delete   --- delete a structure
 
POST  /coworker/add   --- add a coworker to the teamName (return a map with the connections)
DELETE  /coworker/remove/{name}   --- remove a coworker by its name
GET  /coworker/get/all   --- return a map of the connected teams
 
GET  /teamName/get/coworker/{name}   --- return coworkers of the teamName by its name
GET  get/info/teamName/{name}   --- return departmentName and locationName names of given teamName
GET  /teamName/get/all   --- return all teams in the company
POST  /teamName/add   --- add a teamName (using MyRequestObj class)
DELETE  /teamName/remove   --- remove a teamName from the hierarchy
 
GET  /locationName/get/all   --- return all the locations in the company
POST  /locationName/add   --- add a locationName (using MyRequestObj class)
DELETE  /locationName/remove   --- remove a locationName from the hierarchy
 
GET  /departmentName/get/all   --- return all the departments
POST  /departmentName/add   --- add a departmentName (using MyRequestObj class)
DELETE  /departmentName/remove   --- remove a departmentName from the hierarchy
```

### Design overview
Application is based on two classes - **CompanyService** and **CooperationService** which manage
creating and deleting teams, locations, departments and connections between teams



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

##### removing a teamName from hierarchy (teamName will be also removed from any connections)

```
{
    "departmentName": "IT",
    "locationName": "Krakow",
    "teamName": "teamITK6" 
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



### TESTS

Tests cover logic of classes:
- CompanyServiceTest
- CooperationService


### Possible improvements  
Teams can be identified by its name or id. It could be changed to identify teamName only by its name/id.

- migrate Spring configuration to .xml file (for now annotations are fine and don't create chaos)
- refactor methods to increase Java 8 usage (i.e. Optionals?)
- more unit, integration tests
- mapping endpoints

###### Note
In the beginning I started to write app which was connected with database but then I read that application should keep all information about hierarchy tree.
I think I would create app with database because it's more natural for me.
Hope this solution will meets requirements :)