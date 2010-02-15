update bb_message set published = true;
update bb_relationship set acknowledged=true;
update bb_bookmark set acknowledged = true;
update bb_message_recipient set read = true;