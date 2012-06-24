"""
Recycling application utility functions
"""
from geopy.distance import distance
from geopy.point import Point
from geopy.units import km, miles


def bounding_box(latitude, longitude, miles):
    """
    Get the latitude and longitude coordinates of the bounding box which
    contains all points within the specified distance of this location.
    The box is substantially bigger than it needs to be; this is just an
    approximation for the purpose of filtering.
    """
    center = Point(latitude, longitude)
    dist = distance()
    top = dist.destination(center, 0, km(miles=2 * miles))
    r = top.longitude - longitude
    return (latitude - r, longitude + r, latitude + r, longitude - r)


def distance_between(point, latitude, longitude):
    """
    Get the distance from this point to the specified coordinates in miles.
    """
    if latitude is None or longitude is None:
        return 0
    dist = distance()
    kilometers = dist.measure(point, Point(latitude, longitude))
    return miles(kilometers=kilometers)
