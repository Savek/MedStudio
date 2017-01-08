/**
 * Created by Savek on 2017-01-08.
 */
angular
    .module('admin')
    .controller('resultDetailsController', function ($scope, $http, $routeParams) {

        $scope.serviceData = [];

        $http.get('/getResultsDetails/'+ $routeParams.userId + '/' + $routeParams.resultType, {params:{"date": $routeParams.date}})
            .then(function (response) {

                $scope.serviceData = [];
                angular.forEach(response.data, function(value) {

                    $scope.serviceData = response.data;
                });

            }, function () {

            });
    });