from django.http import HttpResponse
from django.shortcuts import render
import json

"""
This is a text translator using Watson API.
"""

import sys
from pubnub.callbacks import SubscribeCallback
from pubnub.enums import PNStatusCategory
from pubnub.pnconfiguration import PNConfiguration
from pubnub.pubnub import PubNub

# PubNub Setup

pnconfig = PNConfiguration()

pnconfig.subscribe_key = 'sub-c-79ad0e10-382b-11e7-ae4f-02ee2ddab7fe'
pnconfig.publish_key = 'pub-c-55835b2a-73bc-42e7-8599-3b399f82fbf7'
pnconfig.ssl = False

channel_detection = 'pubnub-lan-detector'
channel_translation = 'pubnub-translator'

# PubNub Result
result = None

# Dictionary for language to locale
lan_to_locale_dict = {
    'english': 'en',
    'japanese': 'ja',
    'chinese': 'zh',
    'french': 'fr',
    'spanish': 'es'
}

# PubNub Language Detection

pubnub_detection = PubNub(pnconfig)
detection_listener = None


def language_detection_callback(envelope, status):
    # Check whether request successfully completed or not
    if not status.is_error():
        pass  # Message successfully published to specified channel.
    else:
        pass  # Handle message publish error. Check 'category' property to find out possible issue
        # because of which request did fail.
        # Request can be resent using: [status retry];


class LanguageDetectionCallback(SubscribeCallback):
    def __init__(self, text):
        self._text = text

    def presence(self, pubnub, presence):
        pass  # handle incoming presence data

    def status(self, pubnub, status):
        if status.category == PNStatusCategory.PNUnexpectedDisconnectCategory:
            pass  # This event happens when radio / connectivity is lost

        elif status.category == PNStatusCategory.PNConnectedCategory:
            # Connect event. You can do stuff like publish, and know you'll get it.
            # Or just use the connected event to confirm you are subscribed for
            # UI / internal notifications, etc
            pubnub.publish().channel(channel_detection).message({
                'text': self._text
            }).async(language_detection_callback)
        elif status.category == PNStatusCategory.PNReconnectedCategory:
            pass
            # Happens as part of our regular operation. This event happens when
            # radio / connectivity is lost, then regained.
        elif status.category == PNStatusCategory.PNDecryptionErrorCategory:
            pass
            # Handle message decryption error. Probably client configured to
            # encrypt messages and on live data feed it received plain text.

    def message(self, pubnub, message):
        # Handle new message stored in message.message
        res = message.message['text']
        global result
        result = res
        global detection_listener
        pubnub.unsubscribe().channels(channel_detection).execute()
        pubnub.remove_listener(detection_listener)


# PubNub Language Translation

pubnub_translation = PubNub(pnconfig)
translation_listener = None


def language_translation_callback(envelope, status):
    # Check whether request successfully completed or not
    if not status.is_error():
        pass  # Message successfully published to specified channel.
    else:
        print()
        pass  # Handle message publish error. Check 'category' property to find out possible issue
        # because of which request did fail.
        # Request can be resent using: [status retry];


class LanguageTranslationCallback(SubscribeCallback):
    def __init__(self, src, target, text):
        self._src = src
        self._target = target
        self._text = text

    def presence(self, pubnub, presence):
        pass  # handle incoming presence data

    def status(self, pubnub, status):
        if status.category == PNStatusCategory.PNUnexpectedDisconnectCategory:
            pass  # This event happens when radio / connectivity is lost

        elif status.category == PNStatusCategory.PNConnectedCategory:
            # Connect event. You can do stuff like publish, and know you'll get it.
            # Or just use the connected event to confirm you are subscribed for
            # UI / internal notifications, etc
            pubnub.publish().channel(channel_translation).message({
                'src': self._src,
                'target': self._target,
                'text': self._text
            }).async(language_translation_callback)
        elif status.category == PNStatusCategory.PNReconnectedCategory:
            pass
            # Happens as part of our regular operation. This event happens when
            # radio / connectivity is lost, then regained.
        elif status.category == PNStatusCategory.PNDecryptionErrorCategory:
            pass
            # Handle message decryption error. Probably client configured to
            # encrypt messages and on live data feed it received plain text.

    def message(self, pubnub, message):
        # Handle new message stored in message.message
        res = message.message['text']
        global result
        result = res
        global translation_listener
        pubnub.unsubscribe().channels(channel_translation).execute()
        pubnub.remove_listener(translation_listener)


# Public Functions

def detect_languagte(text):
    global detection_listener
    detection_listener = LanguageDetectionCallback(text)
    pubnub_detection.add_listener(detection_listener)
    pubnub_detection.subscribe().channels(channel_detection).execute()


def translate_languagte(src, target, text):
    global translation_listener
    translation_listener = LanguageTranslationCallback(src, target, text)
    pubnub_translation.add_listener(translation_listener)
    pubnub_translation.subscribe().channels(channel_translation).execute()


# Main Function

if __name__ == '__main__':
    argv = sys.argv
    if len(argv) <= 1:
        print('No argument provided for translator.')
    else:
        config = argv[1]
        if config == '--help':
            print("""--detect [text]
--translate [src, target, text]""")
        if config == '--detect':
            detect_languagte(argv[2])
        elif config == '--translate':
            translate_languagte(argv[2], argv[3], argv[4])


# Create your views here.

def translate(request, src, target, text):
    translate_languagte(src, target, text)
    global result
    while result is None:
        pass
    response_data = {
        'src': src,
        'target': target,
        'text': result
    }
    result = None

    return HttpResponse(json.dumps(response_data), content_type="application/json")


def detect(request, text):
    detect_languagte(text)
    global result
    while result is None:
        pass
    response_data = {
        'text': result
    }
    result = None

    return HttpResponse(json.dumps(response_data), content_type="application/json")


def locale(request, language):
    lower_case = language.lower()
    final_key = lower_case.replace(' ', '')
    response_data = {
        'text': lan_to_locale_dict[final_key]
    }
    return HttpResponse(json.dumps(response_data), content_type="application/json")
