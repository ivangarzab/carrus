package com.ivangarzab.carrus

/**
 * Created by Ivan Garza Bermea.
 */
//TODO: Rename & Update all relevant tests with v0 vs v1
val TEST_CAR_JSON = """ 
    {
        "uid":"123",
        "imageUri":"",
        "licenseNo":"DH9⭐L474",
        "make":"Nissan",
        "milesPerGallon":"",
        "model":"Altima",
        "nickname":"",
        "services":[
            {
                "brand":"",
                "cost":80.0,
                "dueDate":{"year":2023,"month":11,"dayOfMonth":31,"hourOfDay":10,"minute":58,"second":1},
                "id":"f07be0d4-b969-40f4-9a47-6285263cf9a2",
                "name":"Car Registration: TX",
                "repairDate":{"year":2022,"month":11,"dayOfMonth":22,"hourOfDay":10,"minute":58,"second":9},
                "type":"",
                "version":1
            },
            {
                "brand":"",
                "cost":250.0,
                "dueDate":{"year":2034,"month":9,"dayOfMonth":7,"hourOfDay":13,"minute":26,"second":12},
                "id":"42f3ba2d-2e03-44de-a103-646d792751be",
                "name":"AC Compressor",
                "repairDate":{"year":2022,"month":8,"dayOfMonth":7,"hourOfDay":13,"minute":26,"second":6},
                "type":"",
                "version":1
            }
        ],
        "tirePressure":"",
        "totalMiles":"",
        "uid":"411ee2d7-03b7-4ccb-b231-97ce81444306",
        "year":"2012"
    }
""".trimIndent()

val TEST_CAR_V0_JSON = """ 
    {
        "uid":"123",
        "nickname":"",
        "make":"Nissan",
        "model":"Altima",
        "year":"2012",
        "licenseNo":"DH9⭐L474",
        "vinNo":"ABCDEFGHI",
        "tirePressure":"32",
        "totalMiles":"100000",
        "milesPerGallon":"26",
        "services":[],
        "imageUri":""
    }
""".trimIndent()

val TEST_CAR_V1_JSON = """ 
    {
        "version":1,
        "uid":"123",
        "nickname":"",
        "make":"Nissan",
        "model":"Altima",
        "year":"2012",
        "licenseState":"Texas",
        "licenseNo":"DH9⭐L474",
        "vinNo":"ABCDEFGHI",
        "tirePressure":"32",
        "totalMiles":"100000",
        "milesPerGalHighway":"26",
        "milesPerGalCity":"26",
        "services":[],
        "imageUri":""
    }
""".trimIndent()
