/**
 * Created by Savek on 2017-01-08.
 */
angular
    .module('admin')
    .controller('resultDetailsController', function ($scope, $filter, $http, $routeParams, RESULT_TYPES) {

        $scope.currentPage = 1;
        $scope.pageChanged = function() {
            $scope.chart();
        };

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
            $scope.resultsDataAll = [];
            $scope.resultsData = [];
            var dataTemp = []; // charts need double arrays, so we need to create one and then push it to main array
            $http.get('/getResultsDetails/'+ $routeParams.userId + '/' + $routeParams.resultType, {params:{"date": $routeParams.date}})
                .then(function (response) {

                    $scope.resultsDataAll = response.data;

                    $scope.resultsData = Object.keys($scope.resultsDataAll).slice(($scope.currentPage - 1) * 10, $scope.currentPage * 10);
                    $scope.totalItems = Object.keys($scope.resultsDataAll).length;

                    if ($routeParams.resultType == RESULT_TYPES.pressure) {

                        $scope.colors = ['#58a554', '#C2639D'];
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
                            },
                            legend: {display: true}
                        };

                        $scope.series = ['Ciśnienie skurczowe', 'Ciśnienie rozkurczowe'];
                        var dataTemp2 = [];
                        angular.forEach($scope.resultsData, function(key) {
                            $scope.labels.push($filter('date')(new Date(key), "HH:mm:ss"));
                            dataTemp.push($scope.resultsDataAll[key].split('/')[0]);
                            dataTemp2.push($scope.resultsDataAll[key].split('/')[1]);
                        });
                        $scope.data.push(dataTemp);
                        $scope.data.push(dataTemp2);

                    } else {
                        angular.forEach($scope.resultsData, function(key) {
                            $scope.labels.push($filter('date')(new Date(key), "HH:mm:ss"));
                            dataTemp.push($scope.resultsDataAll[key]);
                        });
                        $scope.data.push(dataTemp);
                    }
                });
        };
        $scope.chart();

    });