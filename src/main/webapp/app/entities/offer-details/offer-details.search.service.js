(function() {
    'use strict';

    angular
        .module('myappApp')
        .factory('OfferDetailsSearch', OfferDetailsSearch);

    OfferDetailsSearch.$inject = ['$resource'];

    function OfferDetailsSearch($resource) {
        var resourceUrl =  'api/_search/offer-details/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
