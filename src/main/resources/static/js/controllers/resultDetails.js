/**
 * Created by Savek on 2017-01-08.
 */
angular
    .module('admin')
    .controller('resultDetailsController', function ($scope, $http, $routeParams, RESULT_TYPES) {

        $scope.resultsDataAll = [];
        $scope.resultsData = [];
        $http.get('/getResultsDetails/'+ $routeParams.userId + '/' + $routeParams.resultType, {params:{"date": $routeParams.date}})
            .then(function (response) {

                $scope.serviceData = [];
                angular.forEach(response.data, function(value) {

                    $scope.resultsDataAll = response.data;
                    $scope.totalItems = response.data.length;
                    $scope.currentPage = 1;
                    $scope.resultsData = $scope.resultsDataAll.slice(0, 10);
                });

            });

        $scope.pageChanged = function() {
            $scope.resultsData = $scope.resultsDataAll.slice(($scope.currentPage - 1) * 10, $scope.currentPage * 10);
        };
    });