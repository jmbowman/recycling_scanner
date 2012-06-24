from django import forms
from geopy.point import Point
from haystack.forms import SearchForm
from haystack.query import SearchQuerySet

from recycling.utils import bounding_box, distance_between


class TakerSearchForm(SearchForm):
    code = forms.CharField(max_length=15, required=False)
    latitude = forms.FloatField(required=False)
    longitude = forms.FloatField(required=False)
    miles = forms.ChoiceField(choices=(('5', '5'), ('10', '10'), ('25', '25'),
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
        if self.cleaned_data['code']:
            sqs = sqs.filter(code=self.cleaned_data['code'])
        if self.cleaned_data['zip']:
            miles = int(self.cleaned_data.get('miles', '5'))
            # first pass at filtering based on rough bounding box
            latitude = self.cleaned_data.get('latitude', 42.361208)
            longitude = self.cleaned_data.get('longitude', -71.081157)
            bbox = bounding_box(latitude, longitude, miles)
            sqs.filter(latitude__gte=bbox[0])
            sqs.filter(longitude__lte=bbox[1])
            sqs.filter(latitude__lte=bbox[2])
            sqs.filter(longitude__gte=bbox[3])
            # now filter the results for actual distance matches
            ids = []
            center = Point(latitude, longitude)
            for result in sqs.all():
                if distance_between(center, result.latitude, result.longitude) <= miles:
                    ids.append(result.pk)
            if len(ids) > 0:
                sqs = SearchQuerySet().filter(django_id__in=ids)
            else:
                sqs = SearchQuerySet().none()

        return sqs.load_all()
