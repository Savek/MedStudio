/**
 * Created by Savek on 2017-01-25.
 */
angular
    .module('admin')
    .controller('adminHomePageController', function ($scope, $http, $routeParams) {

        $scope.resultCounter;

        $http.get("/getResultsCount")
            .then(function(response) {
                console.log(response.data);
            })
    });