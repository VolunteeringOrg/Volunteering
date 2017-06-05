(function() {
    'use strict';
    angular
        .module('myappApp')
        .factory('OfferDetails', OfferDetails);

    OfferDetails.$inject = ['$resource'];

    function OfferDetails ($resource) {
        var resourceUrl =  'api/offer-details/:id';

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
