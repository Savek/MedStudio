/**
 * Created by Savek on 2017-01-18.
 */
angular
    .module('admin')
    .controller('usersPanelController', function ($scope, $http) {

        $scope.usersSliced = [];
        $scope.usersAll = [];

        $scope.getUsers = function() {

            $http.get('/getUsers').then(function (response) {

                $scope.usersAll = response.data;

                $scope.totalItems = response.data.length;
                $scope.currentPage = 1;
                $scope.usersSliced = $scope.usersAll.slice(0, 10);
            });
        };

        $scope.getUsers();

        $scope.pageChanged = function() {
            $scope.usersSliced = $scope.usersAll.slice(($scope.currentPage - 1) * 10, $scope.currentPage * 10);
        };

        $scope.deleteUser = function(userId) {
            $http.delete('/deleteUserFromDB/' + userId).then(function() {

                $scope.getUsers();
            });
        };
    });