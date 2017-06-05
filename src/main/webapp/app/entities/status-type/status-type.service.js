(function() {
    'use strict';
    angular
        .module('myappApp')
        .factory('StatusType', StatusType);

    StatusType.$inject = ['$resource'];

    function StatusType ($resource) {
        var resourceUrl =  'api/status-types/:id';

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
