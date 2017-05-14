"""
This is a text translator using Watson API.
"""

from pubnub.callbacks import SubscribeCallback
from pubnub.enums import PNStatusCategory
from pubnub.pnconfiguration import PNConfiguration
from pubnub.pubnub import PubNub

# PubNub Setup

pnconfig = PNConfiguration()

pnconfig.subscribe_key = 'sub-c-79ad0e10-382b-11e7-ae4f-02ee2ddab7fe'
pnconfig.publish_key = 'pub-c-55835b2a-73bc-42e7-8599-3b399f82fbf7'
pnconfig.ssl = False


# PubNub Language Detection

pubnub_detection = PubNub(pnconfig)


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
            pubnub.publish().channel("pubnub-lan-detector").message({
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
        print(message.message['text'])


# PubNub Language Translation

pubnub_translation = PubNub(pnconfig)


def language_translation_callback(envelope, status):
    # Check whether request successfully completed or not
    if not status.is_error():
        pass  # Message successfully published to specified channel.
    else:
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
            pubnub.publish().channel("pubnub-translator").message({
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
        print(message.message['text'])


# Public Functions

def detect_languagte(text):
    pubnub_detection.add_listener(LanguageDetectionCallback(text))
    pubnub_detection.subscribe().channels('pubnub-lan-detector').execute()


def translate_languagte(src, target, text):
    pubnub_translation.add_listener(LanguageTranslationCallback(src, target, text))
    pubnub_translation.subscribe().channels('pubnub-translator').execute()


# Main Function

if __name__ == '__main__':
    detect_languagte('你好')
    translate_languagte('en', 'fr', 'What are you doing?')
