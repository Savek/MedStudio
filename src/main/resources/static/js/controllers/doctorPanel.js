/**
 * Created by Savek on 2017-01-15.
 */
angular
    .module('admin')
    .controller('doctorPanelController', function ($scope, $http, $routeParams, RESULT_TYPES) {


        $http.get('/getPatients/' + $routeParams.userId).then(function (response) {

            console.log(response.data);
            $scope.patients = response.data;
        }, function () {

        });
    });