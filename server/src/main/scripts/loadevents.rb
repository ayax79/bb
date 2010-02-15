#!/usr/bin/env ruby

module Event
  require "rexml/document"
  require "net/http"

  class EventPublisher
    include Net

    def initialize(base_url)
      @base_url = base_url
    end

    def post_xml(xml)
      url = URI.parse(@base_url + "/rest/occasions/addfromxml")
      puts url
      request = HTTP::Post.new(url.path)
      request.content_type = 'application/xml'
      request.body = xml.to_s
      response = HTTP.start(url.host, url.port) { |http| http.request(request) }
      case response
        when Net::HTTPSuccess, Net::HTTPRedirection
          # OK
        else
          response.error!
      end
    end
  end

  class EventParser
    def initialize(file)
      file = File.new(file)
      @xml = REXML::Document.new(file)
    end

    def each_event
      @xml.elements.each("events/event") do |event|
        yield event
      end
    end
  end
end

if __FILE__ == $0
  publisher = Event::EventPublisher.new("http://localhost:8081")
  publisher.post_xml IO.read(ARGV[0])

  #Event::EventParser.new(ARGV[0]).each_event do |x|
  #    publisher.post_xml x
  #end
end
