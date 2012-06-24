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
        results = [{'name': o.name} for o in results]
        result = {'page_count': paginator.num_pages, 'results': results}
        return HttpResponse(dumps(result), mimetype='application/json')
