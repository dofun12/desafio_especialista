var stats = {
    type: "GROUP",
name: "Global Information",
path: "",
pathFormatted: "group_missing-name-b06d1",
stats: {
    "name": "Global Information",
    "numberOfRequests": {
        "total": "15055",
        "ok": "15055",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "7",
        "ok": "7",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "47",
        "ok": "47",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "11",
        "ok": "11",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "4",
        "ok": "4",
        "ko": "-"
    },
    "percentiles1": {
        "total": "10",
        "ok": "10",
        "ko": "-"
    },
    "percentiles2": {
        "total": "11",
        "ok": "11",
        "ko": "-"
    },
    "percentiles3": {
        "total": "18",
        "ok": "18",
        "ko": "-"
    },
    "percentiles4": {
        "total": "31",
        "ok": "31",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "count": 15055,
    "percentage": 100
},
    "group2": {
    "name": "800 ms < t < 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group3": {
    "name": "t > 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group4": {
    "name": "failed",
    "count": 0,
    "percentage": 0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "50.351",
        "ok": "50.351",
        "ko": "-"
    }
},
contents: {
"req_create-book-56e35": {
        type: "REQUEST",
        name: "create-book",
path: "create-book",
pathFormatted: "req_create-book-56e35",
stats: {
    "name": "create-book",
    "numberOfRequests": {
        "total": "15055",
        "ok": "15055",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "7",
        "ok": "7",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "47",
        "ok": "47",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "11",
        "ok": "11",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "4",
        "ok": "4",
        "ko": "-"
    },
    "percentiles1": {
        "total": "10",
        "ok": "10",
        "ko": "-"
    },
    "percentiles2": {
        "total": "11",
        "ok": "11",
        "ko": "-"
    },
    "percentiles3": {
        "total": "18",
        "ok": "18",
        "ko": "-"
    },
    "percentiles4": {
        "total": "31",
        "ok": "31",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "count": 15055,
    "percentage": 100
},
    "group2": {
    "name": "800 ms < t < 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group3": {
    "name": "t > 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group4": {
    "name": "failed",
    "count": 0,
    "percentage": 0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "50.351",
        "ok": "50.351",
        "ko": "-"
    }
}
    }
}

}

function fillStats(stat){
    $("#numberOfRequests").append(stat.numberOfRequests.total);
    $("#numberOfRequestsOK").append(stat.numberOfRequests.ok);
    $("#numberOfRequestsKO").append(stat.numberOfRequests.ko);

    $("#minResponseTime").append(stat.minResponseTime.total);
    $("#minResponseTimeOK").append(stat.minResponseTime.ok);
    $("#minResponseTimeKO").append(stat.minResponseTime.ko);

    $("#maxResponseTime").append(stat.maxResponseTime.total);
    $("#maxResponseTimeOK").append(stat.maxResponseTime.ok);
    $("#maxResponseTimeKO").append(stat.maxResponseTime.ko);

    $("#meanResponseTime").append(stat.meanResponseTime.total);
    $("#meanResponseTimeOK").append(stat.meanResponseTime.ok);
    $("#meanResponseTimeKO").append(stat.meanResponseTime.ko);

    $("#standardDeviation").append(stat.standardDeviation.total);
    $("#standardDeviationOK").append(stat.standardDeviation.ok);
    $("#standardDeviationKO").append(stat.standardDeviation.ko);

    $("#percentiles1").append(stat.percentiles1.total);
    $("#percentiles1OK").append(stat.percentiles1.ok);
    $("#percentiles1KO").append(stat.percentiles1.ko);

    $("#percentiles2").append(stat.percentiles2.total);
    $("#percentiles2OK").append(stat.percentiles2.ok);
    $("#percentiles2KO").append(stat.percentiles2.ko);

    $("#percentiles3").append(stat.percentiles3.total);
    $("#percentiles3OK").append(stat.percentiles3.ok);
    $("#percentiles3KO").append(stat.percentiles3.ko);

    $("#percentiles4").append(stat.percentiles4.total);
    $("#percentiles4OK").append(stat.percentiles4.ok);
    $("#percentiles4KO").append(stat.percentiles4.ko);

    $("#meanNumberOfRequestsPerSecond").append(stat.meanNumberOfRequestsPerSecond.total);
    $("#meanNumberOfRequestsPerSecondOK").append(stat.meanNumberOfRequestsPerSecond.ok);
    $("#meanNumberOfRequestsPerSecondKO").append(stat.meanNumberOfRequestsPerSecond.ko);
}
