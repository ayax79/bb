#!/usr/bin/env ruby
count = 1
db = 'blackbox-staging'
description = 'Thank you for being a rockstar founder! xoxo, Blackbox Angels'
IO.popen("psql -t -c 'select guid from bb_user where type = 4' #{db}").readlines.each do |guid|
	puts guid
	begin
		cmd = "psql -c \"insert into bb_vouch (guid, created, modified, version, expirationDate, targetguid, targetownertype, voucherguid, voucherownertype, accepted, count, description) " +
				"values (#{count}, now(), now(), 0, '2050-01-01', '#{guid}', 0, 1, 0, true, 1, '#{description}')\" #{db}"
		puts cmd
		system cmd
	rescue
		puts "an error occurred"
	end
	count = count + 1
end