from haystack import site
from haystack import indexes

from recycling.models import Taker


class TakerIndex(indexes.SearchIndex):
    text = indexes.CharField(document=True, use_template=True)
    code = indexes.CharField(model_attr='code')
    latitude = indexes.FloatField(model_attr='address__latitude', null=True)
    longitude = indexes.FloatField(model_attr='address__longitude', null=True)

    def index_queryset(self):
        """Used when the entire index for model is updated."""
        return Taker.objects.all()

site.register(Taker, TakerIndex)
