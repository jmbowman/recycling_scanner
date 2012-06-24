"""
Recycling application URLs
"""
from django.conf.urls.defaults import patterns, url

from recycling.forms import TakerSearchForm
from recycling.views import TakerSearchView


urlpatterns = patterns('',
    url(r'^search/$', TakerSearchView(form_class=TakerSearchForm),
        name='search'),
)
