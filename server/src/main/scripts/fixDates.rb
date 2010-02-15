#!/usr/bin/env ruby

require "rexml/document"

def date_update(file, &block)
  xml = REXML::Document.new(File.new(file))
  xml.elements.each("events/event") do |event|
    block.call(event)
  end
end

if __FILE__ == $0

  begin
    out = File.new("updates.sql", "w+")
    out.puts "BEGIN;"
    date_update(ARGV[0]) do |event|

      guid = event.elements['guid'].text
      eventtime = event.elements['eventtime'].text
      eventtime = Time.at(eventtime.to_i).asctime

      eventendtime = event.elements['eventendtime'].text
      eventendtime = Time.at(eventendtime.to_i).asctime

      out.puts "UPDATE bb_occasion SET eventendtime = '#{eventendtime}', eventtime = '#{eventtime}' WHERE guid = '#{guid}';"
    end
    out.puts "COMMIT;"
  ensure
    out.close
  end

end