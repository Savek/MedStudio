/**
 * Created by Savek on 2017-01-15.
 */
angular
    .module('admin')
    .controller('doctorPanelController', function ($scope, $http, $routeParams, RESULT_TYPES) {

        $scope.patientsSliced = [];
        $scope.patientsAll = [];

        $scope.getPatients = function() {

            $http.get('/getPatients/' + $routeParams.userId).then(function (response) {

                $scope.patientsAll = response.data;

                $scope.totalItems = response.data.length;
                $scope.currentPage = 1;
                $scope.patientsSliced = $scope.patientsAll.slice(0, 10);
            });
        };

        $scope.getPatients();

        $scope.pageChanged = function() {
            $scope.patientsSliced = $scope.patientsAll.slice(($scope.currentPage - 1) * 10, $scope.currentPage * 10);
        };
    });