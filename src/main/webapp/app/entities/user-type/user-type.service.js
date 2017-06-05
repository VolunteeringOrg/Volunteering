(function() {
    'use strict';
    angular
        .module('myappApp')
        .factory('UserType', UserType);

    UserType.$inject = ['$resource'];

    function UserType ($resource) {
        var resourceUrl =  'api/user-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
