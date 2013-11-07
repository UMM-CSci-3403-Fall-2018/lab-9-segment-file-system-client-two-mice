require 'etc'
require 'aruba/cucumber'

Before do
  @aruba_timeout_seconds = 30
  `mkdir -p tmp/aruba/`
  `cp -R etc tmp/aruba`
  `cp -r ../bin/segmentedserver tmp/aruba`
end

Given /^a file server client$/ do |server, client|
  `etc/copy_client_and_server.sh #{server} #{client}`
  steps %{
    When I run `etc/echo_server.sh`
  }
end

When /^I run the file server client$/ do
  #puts "Running client with input=<#{input}> and output=<#{output}>"
  steps %{
    When I run `etc/run_file_server_client.sh`
  }
  #puts "Client has been run"
end

Then /^the file "([^"]*)" exists$/ do |file_name|
  steps %{
    Then a file named "#{file_name}" should exist
  }
end

Then /^the file "(.*?)" should differ from "(.*?)" only by whitespace$/ do |actual_filename, expected_filename|
  steps %{
    Then I successfully run `diff -wbB #{actual_filename} #{expected_filename}`
  }
end
