from django.http import HttpResponse
from django.shortcuts import render
import json


# Create your views here.

def index(request, **kwargs):
    response_data = {}
    response_data['result'] = 'error'
    response_data['message'] = 'Some error message'
    return HttpResponse(json.dumps(response_data), content_type="application/json")