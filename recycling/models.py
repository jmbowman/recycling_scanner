"""
Identify, Reuse, Recycle database models
"""
from django.db import models


class Address(models.Model):
    address1 = models.CharField(max_length=200, blank=True)
    address2 = models.CharField(max_length=200, blank=True)
    city = models.CharField(max_length=200, blank=True)
    state = models.CharField(max_length=2, blank=True)
    zip_code = models.CharField(max_length=10, blank=True)
    latitude = models.FloatField(null=True, blank=True)
    longitude = models.FloatField(null=True, blank=True)

    def __unicode__(self):
        return self.address1


class Category(models.Model):
    name = models.CharField(max_length=200)

    def __unicode__(self):
        return self.name


class Maker(models.Model):
    name = models.CharField(max_length=200)
    url = models.URLField(verbose_name='URL', blank=True)

    def __unicode__(self):
        return self.name


class Product(models.Model):
    name = models.CharField(max_length=200)
    description = models.CharField(max_length=4000, blank=True)
    category = models.ForeignKey(Category, null=True, blank=True)
    code = models.CharField(max_length=15, blank=True)
    maker = models.ForeignKey(Maker, null=True, blank=True)
    model = models.CharField(max_length=200, blank=True)

    def __unicode__(self):
        return self.name


class Taker(models.Model):
    name = models.CharField(max_length=200)
    address = models.ForeignKey(Address)
    url = models.URLField(verbose_name='URL', blank=True)
    email = models.EmailField(blank=True)
    categories = models.ManyToManyField(Category, blank=True)
    products = models.ManyToManyField(Product, blank=True)

    def __unicode__(self):
        return self.name
