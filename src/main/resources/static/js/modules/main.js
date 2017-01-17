
angular
        .module(
        'main', [ 'ngRoute', 'ngAnimate', 'ngTouch', 'ui.bootstrap', 'util']
        ).config(
            function($routeProvider, $httpProvider) {

                $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

                $routeProvider.when('/', {
                    templateUrl: 'home.html',
                    controller: 'loginController',
                    controllerAs: 'controller'
                }).when('/login', {
                    templateUrl: 'login.html',
                    controller: 'loginController',
                    controllerAs: 'controller'
                }).when('/admin', {
                    templateUrl: 'admin.html',
                }).otherwise('/');
            }
        );

