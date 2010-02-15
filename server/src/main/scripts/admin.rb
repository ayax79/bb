#!/usr/bin/env ruby


module BBAdmin

  require 'net/http'
  require 'rexml/document'

  DEFAULT_PORT = 8081
  DEFAULT_HOST = 'localhost'

  class AdminClient

    def initialize(host)
      @url = host + "/rest/admin"
    end

    def reindex(name)
      index_url = "#{@url}/index/#{name}"
      puts "Calling reindex to url: " + index_url
      handle_delete(index_url)
    end

    def clear_cache(name)
      cache_url = "#{@url}/caches/#{name}"
      puts "Calling clear cache url: " + cache_url
      handle_delete(cache_url)
    end

    def list_caches
      body = handle_get("#{@url}/caches")
      REXML::Document.new(body).elements.each('collection/cacheName/name') do |each|
        puts each.text
      end
    end

    def ping
      puts handle_get "#{@url}/ping"
    end

    private

    def handle_get(path)
      url = URI.parse(path)
      request = Net::HTTP::Get.new(url.path)
      response = Net::HTTP.start(url.host, url.port) { |http| http.request(request) }
      case response
        when Net::HTTPSuccess, Net::HTTPRedirection
          return response.body
        else
          response.error!
      end
    end

    def handle_delete(path)
      url = URI.parse(path)

      request = Net::HTTP::Delete.new(url.path)
      response = Net::HTTP.start(url.host, url.port) { |http| http.request(request) }
      case response
        when Net::HTTPSuccess, Net::HTTPRedirection
          # OK
        else
          response.error!
      end
    end

  end
end


if $0 == __FILE__
  require 'logger'

  rep = BBAdmin::AdminClient.new("http://#{BBAdmin::DEFAULT_HOST}:#{BBAdmin::DEFAULT_PORT}")
  if ARGV[0] == 'reindex'
    puts "Calling index for: " + ARGV[0]
    rep.reindex ARGV[1]
  elsif ARGV[0] == 'clear_cache'
    puts "Calling clear cache: " +ARGV[0]
    rep.clear_cache ARGV[1]
  elsif ARGV[0] == 'list_caches'
    rep.list_caches
  elsif ARGV[0] == 'ping'
    rep.ping
  else
    puts """
Available Commands:
reindex <index name> : rebuild the lucene index
list_caches : lists all ehcache caches
clear_cache <cache name> : remove all entries from the specified cache
    """
    exit 1
  end

end
