--
-- (C) 2013-17 - ntop.org
--

dirs = ntop.getDirs()
package.path = dirs.installdir .. "/scripts/lua/modules/?.lua;" .. package.path
-- io.write ("Session:".._SESSION["session"].."\n")
require "lua_utils"

interface.select(ifname)

if(ntop.isPro()) then
   if interface.isPcapDumpInterface() == false then
      print(ntop.httpRedirect(ntop.getHttpPrefix().."/lua/pro/dashboard.lua"))
      return
   else
      -- it doesn't make sense to show the dashboard for pcap files...
      print(ntop.httpRedirect(ntop.getHttpPrefix().."/lua/if_stats.lua?ifid="..getInterfaceId(ifname)))
      return
   end
end

sendHTTPContentTypeHeader('text/html')

-- The header file for the web GUI is dumped here
ntop.dumpFile(dirs.installdir .. "/httpdocs/inc/header.inc")

-- NOTE: in the home page, footer.lua checks the ntopng version
-- so in case we change it, footer.lua must also be updated
active_page = "home"
dofile(dirs.installdir .. "/scripts/lua/inc/menu.lua")

ifstats = interface.getStats()
is_loopback = isLoopback(ifname)
iface_id = interface.name2id(ifname)

-- Load from or set in redis the refresh frequency for the top flow sankey

refresh = _GET["refresh"]
refresh_key = 'ntopng.prefs.'.._SESSION["user"]..'.'..ifname..'.top_flow_refresh'

if (refresh ~= nil) then
  ntop.setCache(refresh_key,refresh)
else
  refresh = ntop.getCache(refresh_key)
end
-- Default frequency (ms)
if (refresh == '') then refresh = 5000 end

--Possibly redundant code
page = _GET["page"]
if(page == nil) then
   if(not(is_loopback)) then
      page = "TopFlowTalkers"
   else
      page = "TopFlowTalkers"
   end
end

print('<div style="text-align: center;">\n<h3>Summary Report</h3></div>\n')
print('<div style="text-align: center;">\n<h5>Welcome to the summary report page we have modified in ntopng so that the user can see application and device information in one easy to navigate view</h5></div>\n')

--###########################################################
--Starting section for TopFlowTalkers

   print('</ul>\n\t</div>\n\t</nav>\n')

   if(page == "TopFlowTalkers") then
      print('<div style="text-align: left;">\n<h4>Top Flows</h4></div>\n')

      print('<div class="row" style="text-align: center;">')
      dofile(dirs.installdir .. "/scripts/lua/inc/sankey.lua") -- sankey.lua holds flow charts
      
    print('\n</div><br/><br/><br/>\n')

--##########################################################
--Adding in code for TopHosts
    print [[
        <h4>Top Hosts (Send+Receive)</h4>
        <div class="pie-chart" id="topHosts"></div>
        <span class="help-block" style="color: #dddddd;">Click on the host for more information.</span>
        
        <script type='text/javascript'>
            var refresh = 3000 /* ms */;
            do_pie("#topHosts", '/lua/iface_hosts_list.lua', {  }, "", refresh);
        </script>
    ]]

--############################################################################
-- Adding in TopPorts
    print [[
        <table border=0>
        <tr><td>
        <h4>Top Client Ports</h4>
        <div class="pie-chart" id="topClientPorts"></div>
        <span class="help-block" style="color: #dddddd;">Click on the port for more information.</span>
        </td>
        <td>&nbsp;</td>
        <td>
        <h4>Top Server Ports</h4>
        <div class="pie-chart" id="topServerPorts"></div>
        <span class="help-block" style="color: #dddddd;">Click on the port for more information.</span>
        </td></tr>
        </table>

        <script type='text/javascript'>
            var refresh = 3000 /* ms */;
            do_pie("#topClientPorts", '/lua/iface_ports_list.lua', { clisrv: "client" }, "", refresh);
            do_pie("#topServerPorts", '/lua/iface_ports_list.lua', { clisrv: "server" }, "", refresh);
        </script>
    ]]

--#############################################################################
-- Adding in TopApplications
    print [[
        <h4>Top Application Protocols</h4>
        <div class="pie-chart" id="topApplicationProtocols"></div>
        <span class="help-block" style="color: #dddddd;">Click on the application for more information.</span>
        
        <script type='text/javascript'>
            var refresh = 3000 /* ms */;
            do_pie("#topApplicationProtocols", '/lua/iface_ndpi_stats.lua', {  }, "", refresh);
        </script>
    ]]

--#############################################################################
-- Adding in TopASNs
    print [[
        <h4>Top Talker ASNs</h4>
        <div class="pie-chart" id="topASN"></div>

        <script type='text/javascript'>
            var refresh = 3000 /* ms */;
            do_pie("#topASN", '/lua/top_generic.lua?module=top_asn', { senders_receivers : "senders" }, "", refresh);
        </script>
    ]]

--#############################################################################
--Adding in TopFlowSenders
    print [[
        <h4>Top Flow Talkers: Live</h4>
	<div class="pie-chart" id="topSenders"></div>
        <span class="help-block" style="color: #dddddd;">Click on the host for more information.</span>
        
        <script type='text/javascript'>
            var refresh = 3000 /* ms */;
            do_pie("#topSenders", '/lua/top_generic.lua?module=top_talkers', { senders_receivers : "senders" }, "", refresh);
        </script>
    ]]

--#############################################################################
-- Refresh Rates and Footer
print [[
<div class="control-group" style="text-align: center;">
&nbsp;Refresh frequency: <div class="btn-group btn-small">
  <button class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
]]
if (refresh ~= '0') then
  if (refresh == '60000') then
    print('1 Minute')
  else
    print((refresh/1000)..' Seconds ')
  end
else
  print(' Never ')
end

print [[<span class="caret"></span></button>
  <ul class="dropdown-menu ">
]]
print('<li style="text-align: left;"> <a href="'..ntop.getHttpPrefix()..'?refresh=5000" >5 Seconds</a></li>\n')
print('<li style="text-align: left;"> <a href="'..ntop.getHttpPrefix()..'?refresh=10000" >10 Seconds</a></li>\n')
print('<li style="text-align: left;"> <a href="'..ntop.getHttpPrefix()..'?refresh=30000" >30 Seconds</a></li>\n')
print('<li style="text-align: left;"> <a href="'..ntop.getHttpPrefix()..'?refresh=60000" >1 Minute</a></li>\n')
print('<li style="text-align: left;"> <a href="'..ntop.getHttpPrefix()..'?refresh=0" >Never</a></li>\n')
print [[
  </ul>
</div><!-- /btn-group -->
]]

if (refresh ~= '0') then
  print [[
          &nbsp;Live update:  <div class="btn-group btn-group-xs" data-toggle="buttons-radio" data-toggle-name="topflow_graph_state">
            <button id="topflow_graph_state_play" value="1" type="button" class="btn btn-default btn-xs active" data-toggle="button" ><i class="fa fa-play"></i></button>
            <button id="topflow_graph_state_stop" value="0" type="button" class="btn btn-default btn-xs" data-toggle="button" ><i class="fa fa-stop"></i></button>
          </div>
  ]]
else
  print [[
         &nbsp;Refresh:  <div class="btn-group btn-small">
          <button id="topflow_graph_refresh" class="btn btn-default btn-xs">
            <i rel="tooltip" data-toggle="tooltip" data-placement="top" data-original-title="Refresh graph" class="glyphicon glyphicon-refresh"></i></button>
          </div>
  ]]
  end
print [[
</div>
]]

print [[
      <script>
      // Stop sankey interval in order to change the default refresh frequency
      clearInterval(sankey_interval);
]]

if (refresh ~= '0') then
  print ('sankey_interval = window.setInterval(sankey,'..refresh..');')
end

print [[
         var topflow_stop = false;
         $("#topflow_graph_state_play").click(function() {
            if (topflow_stop) {
               sankey();
               sankey_interval = window.setInterval(sankey, 5000);
               topflow_stop = false;
               $("#topflow_graph_state_stop").removeClass("active");
               $("#topflow_graph_state_play").addClass("active");
            }
         });
         $("#topflow_graph_state_stop").click(function() {
            if (!topflow_stop) {
               clearInterval(sankey_interval);
               topflow_stop = true;
               $("#topflow_graph_state_play").removeClass("active");
               $("#topflow_graph_state_stop").addClass("active");
            }
        });
        $("#topflow_graph_refresh").click(function() {
          sankey();
        });

      </script>
      ]]



--######################################################################
--else if not TopFlowTalkers then open index to the active page

   --else
      --ntop.dumpFile(dirs.installdir .. "/httpdocs/inc/index_" .. page .. ".inc")
   --end

--#######################################################################

  -- ntop.dumpFile(dirs.installdir .. "/httpdocs/inc/index_top.inc")
  -- ntop.dumpFile(dirs.installdir .. "/httpdocs/inc/index_bottom.inc")
else
   print("<div class=\"alert alert-warning\">No packet has been received yet on interface " .. getHumanReadableInterfaceName(ifname) .. ".<p>Please wait <span id='countdown'></span> seconds until this page reloads.</div> <script type=\"text/JavaScript\">(function countdown(remaining) { if(remaining <= 0) location.reload(true); document.getElementById('countdown').innerHTML = remaining;  setTimeout(function(){ countdown(remaining - 1); }, 1000);})(10);</script>")
end

dofile(dirs.installdir .. "/scripts/lua/inc/footer.lua")
