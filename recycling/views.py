"""
Recycling application views
"""
from django.http import HttpResponse
from django.utils.simplejson import dumps

from haystack.views import SearchView


class TakerSearchView(SearchView):
    """
    Customized search view that returns results as a JSON list
    """

    def create_response(self):
        """
        Generates the actual HttpResponse to send back to the user.
        """
        (paginator, page) = self.build_page()
        results = page.object_list
        results = [{
                    'name': o.object.name,
                    'address': ', '.join([o.object.address.address1, o.object.address.city, o.object.address.state]),
                    'distance': o.distance if hasattr(o, 'distance') else None,
                    'url': o.object.url
                    } for o in results]
        result = {'page_count': paginator.num_pages, 'results': results}
        return HttpResponse(dumps(result), mimetype='application/json')
