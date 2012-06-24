from django import forms
from geopy.point import Point
from haystack.backends import SQ
from haystack.forms import SearchForm
from haystack.query import SearchQuerySet

from recycling.utils import bounding_box, distance_between


class TakerSearchForm(SearchForm):
    latitude = forms.FloatField(required=False)
    longitude = forms.FloatField(required=False)
    radius = forms.ChoiceField(choices=(('5', '5'), ('10', '10'), ('25', '25'),
                                        ('50', '50')), required=False)

    def no_query_found(self):
        """
        Determines the behavior when no query was found.
        """
        return self.searchqueryset.all()

    def search(self):
        # First, store the SearchQuerySet received from other processing.
        sqs = super(TakerSearchForm, self).search()
        if not hasattr(self, 'cleaned_data'):
            return sqs
        miles = int(self.cleaned_data['radius'])
        # first pass at filtering based on rough bounding box
        latitude = self.cleaned_data['latitude']
        if not latitude:
            latitude = 42.361208
        longitude = self.cleaned_data['longitude']
        if not longitude:
            longitude = -71.081157
        print sqs
        bbox = bounding_box(latitude, longitude, miles)
        sqs = sqs.filter(SQ(latitude__gte=bbox[0]) | SQ(latitude__gt=199))
        sqs = sqs.filter(SQ(longitude__lte=bbox[1]) | SQ(longitude__gt=199))
        sqs = sqs.filter(SQ(latitude__lte=bbox[2]) | SQ(latitude__gt=199))
        sqs = sqs.filter(SQ(longitude__gte=bbox[3]) | SQ(longitude__gt=199))
        print sqs
        # now filter the results for actual distance matches
        ids = []
        center = Point(latitude, longitude)
        for result in sqs.all():
            if result.latitude > 199 or result.longitude > 199:
                # include results without locations, they'll appear last
                ids.append(result.pk)
            if distance_between(center, result.latitude, result.longitude) <= miles:
                ids.append(result.pk)
        if len(ids) > 0:
            sqs = SearchQuerySet().filter(django_id__in=ids)
        else:
            sqs = SearchQuerySet().none()

        results = list(sqs.load_all())
        for taker in results:
            if taker.latitude < 199 and taker.longitude < 199:
                taker.distance = distance_between(center, taker.latitude, taker.longitude)
        results = sorted(results, key=lambda o: o.distance if hasattr(o, 'distance') else 1000000)
        return results
