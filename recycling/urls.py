"""
Recycling application URLs
"""
from django.conf.urls.defaults import patterns, url

from haystack.query import SearchQuerySet
from haystack.views import SearchView
from recycling.forms import TakerSearchForm
from recycling.models import Taker
from recycling.views import TakerSearchView


sqs = SearchQuerySet().models(Taker)
urlpatterns = patterns('',
    url(r'^search/$', TakerSearchView(form_class=TakerSearchForm,
                                      searchqueryset=sqs),
        name='search'),
    url(r'^test/$', SearchView(form_class=TakerSearchForm,
                               searchqueryset=sqs),
        name='search'),
)
