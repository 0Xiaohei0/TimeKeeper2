package com.example.timekeeper2

class timeDataStructure {
    var id : Int = 0
    var date : String = ""
    var timeAdded : String = ""
    var timeString : String = ""
    var time : Int = 0
    constructor(date:String,timeAdded:String,timeString:String, time:Int){
        this.date = date
        this.timeAdded = timeAdded
        this.timeString = timeString
        this.time = time
    }
    constructor(){
    }
}