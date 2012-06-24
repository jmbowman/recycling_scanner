from haystack import site
from haystack import indexes

from recycling.models import Taker


class TakerIndex(indexes.SearchIndex):
    text = indexes.CharField(document=True, use_template=True)
    latitude = indexes.FloatField(model_attr='address__latitude', null=True)
    longitude = indexes.FloatField(model_attr='address__longitude', null=True)

    def prepare_latitude(self, obj):
        """ Use invalid 200 value instead of untestable null """
        address = obj.address
        latitude = address.latitude if address else None
        return 200 if latitude is None else latitude

    def prepare_longitude(self, obj):
        """ Use invalid 200 value instead of untestable null """
        address = obj.address
        longitude = address.longitude if address else None
        return 200 if longitude is None else longitude

    def index_queryset(self):
        """Used when the entire index for model is updated."""
        return Taker.objects.all()

site.register(Taker, TakerIndex)
