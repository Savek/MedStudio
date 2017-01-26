/**
 * Created by Savek on 2017-01-25.
 */
angular
    .module('admin')
    .controller('adminHomePageController', function ($scope, $http, $filter) {


        $http.get("/getResultsCount")
            .then(function(response) {
                $scope.resultCounter = response.data;
            });

        $http.get("/getUsersCounter")
            .then(function(response) {
                $scope.userCounter = response.data;
            });

        $http.get("/getUsersLast7days")
            .then(function(response) {
                $scope.datasetOverride = [{yAxisID: 'y-axis-1'}];

                $scope.options = {
                    scales: {
                        yAxes: [
                            {
                                id: 'y-axis-1',
                                type: 'linear',
                                display: true,
                                position: 'left'
                            }
                        ]
                    }
                };
                $scope.colors = ['#58a554'];
                $scope.chart = function() {

                    $scope.labels = [];
                    $scope.data = [];
                    var dataTemp = [];
                    $scope.totalItems = Object.keys(response.data).length;
                    var now = new Date();
                    var past7day = new Date();
                    past7day.setDate(now.getDate() - 7);
                    for (past7day; past7day <= now; past7day.setDate(past7day.getDate() + 1)) {
                        var value = response.data[past7day.getDate()];
                        $scope.labels.push($filter('date')(past7day, "dd-MM"));
                        if (angular.isUndefined(value)) {
                            value = 0;
                        }
                        dataTemp.push(value);
                    }
                    $scope.data.push(dataTemp);
                };

                $scope.chart();
            });
    });