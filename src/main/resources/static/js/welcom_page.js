/**
 * Created by Savek on 2016-11-27.
 */
angular.module('main')
    .controller('slider',
        function($scope, $http, $location) {
            $scope.image = '1';
            function doSomething() {
                if($scope.autoChanges && $('.modal:visible').length == 0){
                    switch($scope.image){
                        case "1":
                            $scope.image = "2";
                            break;
                        case "2":
                            $scope.image = "3";
                            break;
                        case "3":
                            $scope.image = "4";
                            break;
                        default:
                            $scope.image = "1";
                    }
                }
                $timeout(doSomething, 1500 + getRandomInt(1000));
            }
            $timeout(doSomething, 1500 + getRandomInt(1000));
        })