db.measure.aggregate(
    [
        {
            $match: {
                timestamp: {
                    $gte: ISODate("2020-11-10T01:35:00Z"),
                    $lt: ISODate("2020-12-24T20:59:50Z")
                }
            },
        }, {
            $group: {
                _id: "$customerId",
                energyUsed: { $sum: "$energyUsed" }

            }
        }

    ])



db.measure.aggregate(
    [
        {
            $match: {
                timestamp: {
                    $gte: ISODate("2020-11-10T01:35:00Z"),
                    $lt: ISODate("2020-12-24T20:59:50Z")
                },
                customerId: "61700cba3c5161188dee44bc"
            },
        }, {
            $group: {
                _id: { $dateToString: { format: "%H %u", date: "$timestamp" } },
                energyUsed: { $sum: "$energyUsed" }

            }
        },
        {
            $project: {
                _id: 0,
                energyUsed: 1,
                label: "$_id"
            }
        }

    ])


db.measure.aggregate(
    [
        {
            $match: {
                timestamp: {
                    $gte: ISODate("2020-11-10T01:35:00Z"),
                    $lt: ISODate("2020-12-24T20:59:50Z")
                }

            },
        }, {
            $group: {
                _id: "$region",
                energyUsed: { $sum: "$energyUsed" }

            }
        },
        {
            $project: {
                _id: 0,
                energyUsed: 1,
                region: "$_id"
            }
        }

    ])




db.measure.aggregate(
    [
        {
            $match: {
                timestamp: {
                    $gte: "?0",
                    $lt: "?1"
                }

            },
        }, {
            $group: {
                _id: "$customerId",
                energyUsed: { $sum: "$energyUsed" }

            }
        },
        {
            $project: {
                _id: 0,
                energyUsed: 1,
                date: "?2"
            }
        }

    ])
db.measure.aggregate(
    [
        {
            $match: {
                timestamp: {
                    $gte: ISODate("2015-11-10T01:35:00Z"),
                    $lt: ISODate("2025-12-24T20:59:50Z"),
                },
                region: "A"
            },
        }, {
            $group: {
                _id: "$region",
                energyUsed: { $sum: "$energyUsed" }

            }
        }, { $project: { _id: 0, energyUsed: 1, region: "$_id" } }

    ])

db.dailyCustomerMeasure.insert({

    customerId: "bishop",

    date: "2018-08-20",

    region: "A",
    energyUsed: 456,
    _class="com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.DailyCustomerMeasure"
})

db.dailyCustomerMeasure.aggregate([
    { $match: { date: { $gte: "2000-11-10", $lt: "2025-12-24" }, region: "A" } },
    { $group: { _id: "$region", energyUsed: { $sum: "$energyUsed" } } },
    { $project: { _id: 0, region: "$_id", energyUsed: 1 } }
])


db.CustomerProductionSold.aggregate([
    
])