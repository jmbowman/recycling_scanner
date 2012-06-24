"""
Recycling application views
"""
from django.core import serializers
from django.http import HttpResponse

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
        return HttpResponse(serializers.serialize("json", result),
                            mimetype='application/json')
