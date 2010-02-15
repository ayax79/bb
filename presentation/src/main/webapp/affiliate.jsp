<%--@elvariable id="actionBean" type="com.blackbox.presentation.action.user.AffiliateSettingsActionBean"--%>
<%@include file="/WEB-INF/jsp/include/page_header.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Blackbox Partner Resources</title>
    <link rel="stylesheet" href="${bb:libraryResource('/library/css/settings.css')}" type="text/css"
          media="screen, projection"/>
    <link rel="stylesheet" href="${bb:libraryResource('/library/css/partner.css')}" type="text/css"
          media="screen, projection"/>
    <link rel="stylesheet" href="${bb:libraryResource('/library/css/register.css')}" type="text/css"
          media="screen, projection"/>

    <script src="${bb:libraryResource('/library/js/jquery.maskedinput-1.2.2.min.js')}" type="text/javascript"></script>
    <script type="text/javascript">
        var username = '${actionBean.currentUser.username}';
        var baseurl = '${bb:getProperty('presentation.url')}';
        var imgurl = baseurl + '/library/images/partner/';
        var partnerurl = "http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}";

        var badgesnippet;
        $(function() {
            $("#partner-url").html(partnerurl);
            $(".badge").each(function() {
                $(this).click(function() {
                    badgesnippet = '&lt;a href="' + partnerurl + '"&gt;&lt;img src="' + this.src + '" alt="Join the Republic"/&gt;&lt;/a&gt;';
                    $("#partner-badge-snippet").html(badgesnippet);
                });
            });
        });

        $(function() {
            $.each($(".embed"), function() {
                var embedClone = $(this).find(".embed-template").clone();
                var img = embedClone.find("img");
                img.attr("src", img.attr("title")).removeAttr("title")
                $(this).find("textarea").val(embedClone.html().replace(/[\t\r\n]/g, ""));
            });
        });

    </script>
</head>

<body>
<div class="container container-top"></div>
<div class="container darken">
<div class="main-content">
<h1 class="white">Partner Marketing Toolkit</h1>

<div class="widget-top">
    <div class="widget-top-right"></div>
</div>
<div class="span-24 main-col settings">
<div class="settings-content" id="partner-resources">
<div class="partner-padding clearfix">
    <img src="${bb:libraryResource('/library/images/partner/bbr-logo.png')}" alt="Blackbox Republic logo"
         class="bbr-logo"/>
         
    <p>Looking for the <a href="http://blackboxrepublic.com/welcome/press">Press page</a>?</p>

    <h2 class="partner">Help us build something legendary and make up to $30 per person you send us.</h2>

    <p>Blackbox Republic is a safe, private place for all orientations, relationship combinations and lifestyles to meet&#8212;online
        and in-person. Our audience is the Millennial generation and as a partner who caters to this audience, you can
        create a new hub for your community, find more people to connect with, increase regular attendance at your
        events, and get cash for your cause.
    </p>

    <p class="orange-intro">We think of you guys as part of our team, and to get us all focused on doing something
        legendary, <strong>we&#8217;re going to pay you every two weeks, just like an employee</strong>. The Republic is
        rapidly growing so the faster you get the word out, the more opportunity you have to profit.</p>

    <div class="clearfix">
        <div class="left-narrow"><img src="library/images/partner/narrow-sliders.png" alt="narrow-sliders" width="146"
                                      height="250"/></div>
        <div class="right-basic">
            <p style="margin:0;"><strong>We&#8217;ll credit you 20% of the membership fee for up to six months or up to
                $30 per user.</strong><br/>
            <ul class="nomargin">
                <li>Think about it. If only 1,000 people from your network sign up for 6 months,<br/> you could get
                    $30,000 for you to use any way you wish.
                </li>
                <li>For questions regarding the partner program, e-mail <a href="mailto:partners@blackboxrepublic.com">partners@blackboxrepublic.com</a>.
                </li>
            </ul>

            <p><strong>This page has everything you need to take all the credit.</strong><br/>
                This page has everything you need to reach your audience, send them our way, and get all the credit.
                Whether it be promoting us on your website, including us in your e-mail campaigns, including us in your
                events, or writing about us in your blog, you can use every opportunity to increase your social media
                footprint and generate revenue for yourself. </p>

            <p><strong>Here&#8217;s how:</strong><br/>
                Create a promotion that fits your unique style and use your unique URL to get credit for members who
                sign up through your link. When you download creative assets from this page, they&#8217;ll be
                automatically coded for you. Use them to create a promotion that suits your unique marketing approach.
                You can copy your custom URL from the box below, or have it emailed to you! Use any one of the creative
                assets below or create your own by using the copy and images attached.</p>
        </div>
    </div>

</div>

<h2 class="partner marketing-style">CREATE A MARKETING PLAN THAT FITS YOUR STYLE!</h2>

<div class="partner-padding clearfix">

    <div class="left">

        <h3>E-mail Promotions</h3>

        <p>Do you have a great e-mail list of people you know who&#8217;d like to join us? Select one of these e-mail
            templates, some images and then go for it. There&#8217;s a specific one for event promoters and even one for
            online marketers. </p>

        <p>Or, for even higher impact, create a personalized one with your own special flair. Feel free to use whatever
            language you&#8217;d like just don&#8217;t forget to include a unique offer that ties back to your business
            so your audience will be motivated to use your unique URL.</p>

    </div>

    <div class="right">
        <p class="snippet-int">You can copy your custom URL from the box below, or have it emailed to you:</p>

        <div id="partner-url" class="snippet"></div>
        <%-- Copy and email buttons phase 2? --%>
        <div class="buttons">
            <%--
                                    <button id="partner-url-clipboard-copy"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
            <button id="email"><img src="${bb:libraryResource('/library/images/partner/email-button.png')}"
                                    alt="Email this to me"/></button>

        </div>
        <div class="clear"></div>
        <p><strong>Where does my URL send folks?</strong><br/>
We send folks straight to the BBR homepage. From there, they can learn more or register right away. Don't worry, we've already logged your code. In fact, we attach a cookie to this user so if they decide to register later, you'll still get the credit.</p>
    </div>

</div>

<div class="fullwidth-bg">

    <div class="paper-bg">
        <div class="paper-bg-header">
            <h2 class="partner">Promoter:</h2>
        </div>
        <div class="paper-bg-body">
            <p><strong>Subject Line:</strong> Ok, you guys HAVE TO check this out</p>

            <p>Dear ( ),</p>

            <p>I know you guys hear about all sorts of cool stuff but we HAD TO tell you about something too cool to
                pass up.</p>

            <p>There&#8217;s a new private, invitation-only social hookup site called Blackbox Republic that&#8217;s
                totally focused on people like us&#8212;for all orientations, relationship combinations and lifestyles
                to meet&#8212;online and in person. </p>

            <p>It&#8217;s a totally new experience for people like you to expand their social circle, freely express
                themselves, and find fun new ways to meet offline. Unlike dating sites and social networks that leave
                you wanting more, Blackbox Republic lets you surround yourself with people you truly enjoy and get
                exactly what you&#8217;re looking for whether it be dates, new friends, new partners or a damn good
                party. All the members are vouched for which means no creeps and no judging.</p>

            <p>At Blackbox Republic, you can keep in touch with our community and see first-hand when we announce new
                events, find out who&#8217;s going and share experiences after the festivities. You&#8217;ll also find
                other great events, DJs, entertainers, and associations like ours to expand your world and connect with
                more people like you.</p>

            <p>Click here (insert your URL) to become a member today and get into our next event free.* Just bring a
                print out of your registration confirmation page.</p>

            <p>Come as you are, join your friends, and meet more people like you today. Let the adventure begin.<br/>
                I know we plan to spend a ton of time inside Blackbox Republic. Join us there!</p>

            <p>See you there soon,<br/>
                (Promoter name)</p>

        </div>
        <div class="paper-bg-footer">
            <p>*You must register from this URL (insert URL) to be eligible for this offer.</p>
        </div>
    </div>

</div>

<div class="fullwidth-bg" style="border-bottom:1px solid #ccc;">

    <div class="paper-bg">
        <div class="paper-bg-header">
            <h2 class="partner">Online Marketer:</h2>
        </div>
        <div class="paper-bg-body">
            <p><strong>Subject Line:</strong> Ok, you guys HAVE TO check this out</p>

            <p>Dear ( ),</p>

            <p>I know you guys hear about all sorts of cool stuff but we HAD TO tell you about something too cool to
                pass up.</p>

            <p>There&#8217;s a new private, invitation-only social hookup site called Blackbox Republic that&#8217;s
                totally focused on people like us&#8212;for all orientations, relationship combinations and lifestyles
                to meet—online and in person.</p>

            <p>It&#8217;s a totally new experience for people like you to expand their social circle, freely express
                themselves, and find fun new ways to meet offline. Unlike dating sites and social networks that leave
                you wanting more, Blackbox Republic lets you surround yourself with people you truly enjoy and get
                exactly what you&#8217;re looking for whether it be dates, new friends, new partners or a damn good
                party. All the members are vouched for which means no creeps and no judging.</p>

            <p>At Blackbox Republic, you can keep in touch with our community and see first-hand when we announce new
                events, find out who&#8217;s going and share experiences after the festivities. You&#8217;ll also find
                other great events, DJs, entertainers, and associations like ours to expand your world and connect with
                more people like you.</p>

            <p>At Blackbox Republic you can:</p>
            <ul class="nomargin">
                <li>Be yourself, whatever that means right now.</li>
                <li>Find people who fit your mood.</li>
                <li>Freely express yourself and make it as private or public as you want (post to Facebook, Twitter, and
                    MySpace all from within BBR).
                </li>
                <li>Get access to great events, artists, entertainers, educators and other creative forces.</li>
                <li>Find new events or plan your own.</li>
            </ul>

            <p>It&#8217;s a great way to let go and be yourself, all from one place.</p>

            <p>Click here to join today (insert your URL) and we&#8217;ll give you (insert your offer here).*</p>

            <p>Sincerely,<br/>
                (Partner name)</p>


        </div>
        <div class="paper-bg-footer">
            <p>*You must register from this URL (insert URL) to be eligible for this offer.</p>
        </div>
    </div>

</div>

<div class="clearfix" style="margin-top:20px;">
    <div class="left">
        <h3>Online Marketing:</h3>

        <p>Grab these badges, banners, and tiles for your site or blog, a friend&#8217;s blog, a social network, or
            anywhere you want to reach people connected to you. </p>

        <p>Make sure you copy the code exactly as shown below, this will make sure that every sign-up that comes from
            people clicking through your link puts money right into your account! If you want make your links work
            harder, blog about us using the e-mail copy we&#8217;ve provided. Make sure you check back often, as we&#8217;re
            constantly refining the creative assets to make sure they are working hard to attract the right
            audience.</p>
    </div>

    <div class="right">

    </div>
</div>

<div class="fullwidth-bg padding-l clearfix">

    <h2 class="partner">Animated banners:</h2>

    <div class="partner-banner-download first">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/160x600_lovin-thumb.jpg')}" alt=""/>

        <div class="embed">
            <h4>LOVIN <span>(160X600)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=160x600_lovin.gif" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>

        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/160x600_lovin.gif')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

    <div class="partner-banner-download">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/300x250-lovin-thumb.jpg')}" alt=""/>

        <div class="embed">
            <h4>LOVIN <span>(300X250)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=300x250-lovin.gif" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>
        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/300x250-lovin.gif')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

    <div class="partner-banner-download first">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/728x90-lovin-thumb.jpg')}" alt=""/>

        <div class="embed">
            <h4>LOVIN <span>(728X90)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=728x90-lovin.gif" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>

        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/728x90-lovin.gif')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

    <div class="partner-banner-download first">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/160x600-partners-thumb.jpg')}" alt=""/>

        <div class="embed">
            <h4>PARTNERS <span>(160X600)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=160x600-partners.gif" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>

        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/160x600-partners.gif')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

    <div class="partner-banner-download">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/300x250-partners-thumb.jpg')}" alt=""/>

        <div class="embed">
            <h4>PARTNERS <span>(300X250)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=300x250-partners.gif" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>
        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/300x250-partners.gif')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

    <div class="partner-banner-download first">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/728x90-partners-thumb.jpg')}" alt=""/>

        <div class="embed">
            <h4>PARTNERS <span>(728X90)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=728x90-partners.gif" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>

        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/728x90-partners.gif')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

    <div class="partner-banner-download first">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/160x600-status-thumb.jpg')}" alt=""/>

        <div class="embed">
            <h4>STATUS <span>(160X600)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=160x600-status.gif" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>

        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/160x600-status.gif')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

    <div class="partner-banner-download">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/300x250-status-thumb.jpg')}" alt=""/>

        <div class="embed">
            <h4>STATUS <span>(300X250)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=300x250-status.gif" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>
        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/300x250-status.gif')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

    <div class="partner-banner-download first">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/728x90-status-thumb.jpg')}" alt=""/>

        <div class="embed">
            <h4>STATUS <span>(728X90)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=728x90-status.gif" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>

        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/728x90-status.gif')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

</div>


<div class="fullwidth-bg padding-l clearfix">

    <h3>Static Banners (jpg)</h3>

    <h2 class="partner">Single Female:</h2>

    <div class="partner-banner-download first">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/728x90-singlewoman-thumb.jpg')}"
             alt=""/>

        <div class="embed">
            <h4>LEADERBOARD <span>(728X90)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=728x90-singlewoman.jpg" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>

        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/728x90-singlewoman.jpg')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

    <div class="partner-banner-download">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/160x600-singlewoman-thumb.jpg')}"
             alt=""/>

        <div class="embed">
            <h4>WIDE SKYSCRAPER <span>(160x600)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=160x600-singlewoman.jpg" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>

        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/160x600-singlewoman.jpg')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

    <div class="partner-banner-download first">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/300x250-singlewoman-thumb.jpg')}"
             alt=""/>

        <div class="embed">
            <h4>RECTANGLE <span>(300x250)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=300x250-singlewoman.jpg" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>

        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/300x250-singlewoman.jpg')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

    <div class="partner-banner-download">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/180x150-singlewoman.jpg')}"
             alt=""/>

        <div class="embed">
            <h4>SMALLER RECTANGLE <span>(180x150)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=180x150-singlewoman.jpg" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>
    </div>

    <div class="partner-banner-download">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/125x125-singlewoman.jpg')}"
             alt=""/>

        <div class="embed">
            <h4>SQUARE <span>(125x125)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=125x125-singlewoman.jpg" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>
    </div>

</div>

<div class="fullwidth-bg padding-l clearfix">

    <h2 class="partner">Multiples:</h2>

    <div class="partner-banner-download first">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/728x90-3some-thumb.jpg')}" alt=""/>

        <div class="embed">
            <h4>LEADERBOARD <span>(728X90)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=728x90-3some.jpg" alt="Join the Republic"/></a></textarea>
            <%--<button class="banner-clipboard"><img
                    src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}"
                    alt="Copy to Clipboard"/></button>--%>
        </div>

        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/728x90-3some.jpg')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

    <div class="partner-banner-download">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/160x600-3some-thumb.jpg')}"
             alt=""/>

        <div class="embed">
            <h4>WIDE SKYSCRAPER <span>(160x600)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=160x600-3some.jpg" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>

        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/160x600-3some.jpg')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

    <div class="partner-banner-download first">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/300x250-3some-thumb.jpg')}"
             alt=""/>

        <div class="embed">
            <h4>RECTANGLE <span>(300x250)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=300x250-3some.jpg" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>

        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/300x250-3some.jpg')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

    <div class="partner-banner-download">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/180x150-3some.jpg')}" alt=""/>

        <div class="embed">
            <h4>SMALLER RECTANGLE <span>(180x150)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=180x150-3some.jpg" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>
    </div>

    <div class="partner-banner-download">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/125x125-3some.jpg')}" alt=""/>

        <div class="embed">
            <h4>SQUARE <span>(125x125)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=125x125-3some.jpg" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>
    </div>

</div>

<div class="fullwidth-bg padding-l clearfix">

    <h2 class="partner">Gay Male:</h2>

    <div class="partner-banner-download first">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/728x90-gayman-thumb.jpg')}"
             alt=""/>

        <div class="embed">
            <h4>LEADERBOARD <span>(728X90)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=728x90-gayman.jpg" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>

        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/728x90-gayman.jpg')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

    <div class="partner-banner-download">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/160x600-gayman-thumb.jpg')}"
             alt=""/>

        <div class="embed">
            <h4>WIDE SKYSCRAPER <span>(160x600)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=160x600-gayman.jpg" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>

        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/160x600-gayman.jpg')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

    <div class="partner-banner-download first">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/300x250-gayman-thumb.jpg')}"
             alt=""/>

        <div class="embed">
            <h4>RECTANGLE <span>(300x250)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=300x250-gayman.jpg" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>

        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/300x250-gayman.jpg')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

    <div class="partner-banner-download">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/180x150-gayman.jpg')}" alt=""/>

        <div class="embed">
            <h4>SMALLER RECTANGLE <span>(180x150)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=180x150-gayman.jpg" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>
    </div>

    <div class="partner-banner-download">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/125x125-gayman.jpg')}" alt=""/>

        <div class="embed">
            <h4>SQUARE <span>(125x125)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=125x125-gayman.jpg" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>
    </div>

</div>

<div class="fullwidth-bg padding-l clearfix">

    <h2 class="partner">lesbian:</h2>

    <div class="partner-banner-download first">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/728x90-lesbian-thumb.jpg')}"
             alt=""/>

        <div class="embed">
            <h4>LEADERBOARD <span>(728X90)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=728x90-lesbian.jpg" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>

        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/728x90-lesbian.jpg')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

    <div class="partner-banner-download">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/160x600-lesbian-thumb.jpg')}"
             alt=""/>

        <div class="embed">
            <h4>WIDE SKYSCRAPER <span>(160x600)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=160x600-lesbian.jpg" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>

        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/160x600-lesbian.jpg')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

    <div class="partner-banner-download first">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/300x250-lesbian-thumb.jpg')}"
             alt=""/>

        <div class="embed">
            <h4>RECTANGLE <span>(300x250)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=300x250-lesbian.jpg" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>

        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/300x250-lesbian.jpg')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

    <div class="partner-banner-download">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/180x150-lesbian.jpg')}" alt=""/>

        <div class="embed">
            <h4>SMALLER RECTANGLE <span>(180x150)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=180x150-lesbian.jpg" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>
    </div>

    <div class="partner-banner-download">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/125x125-lesbian.jpg')}" alt=""/>

        <div class="embed">
            <h4>SQUARE <span>(125x125)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=125x125-lesbian.jpg" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>
    </div>

</div>

<div class="fullwidth-bg padding-l clearfix">

    <h2 class="partner">Single Male:</h2>

    <div class="partner-banner-download first">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/728x90-singleman-thumb.jpg')}"
             alt=""/>

        <div class="embed">
            <h4>LEADERBOARD <span>(728X90)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=728x90-singleman.jpg" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>

        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/728x90-singleman.jpg')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

    <div class="partner-banner-download">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/160x600-singleman-thumb.jpg')}"
             alt=""/>

        <div class="embed">
            <h4>WIDE SKYSCRAPER <span>(160x600)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=160x600-singleman.jpg" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>

        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/160x600-singleman.jpg')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

    <div class="partner-banner-download first">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/300x250-singleman-thumb.jpg')}"
             alt=""/>

        <div class="embed">
            <h4>RECTANGLE <span>(300x250)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=300x250-singleman.jpg" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>

        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/300x250-singleman.jpg')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

    <div class="partner-banner-download">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/180x150-singleman.jpg')}"
             alt=""/>

        <div class="embed">
            <h4>SMALLER RECTANGLE <span>(180x150)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=180x150-singleman.jpg" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>
    </div>

    <div class="partner-banner-download">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/125x125-singleman.jpg')}"
             alt=""/>

        <div class="embed">
            <h4>SQUARE <span>(125x125)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=125x125-singleman.jpg" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>
    </div>

</div>

<div class="fullwidth-bg padding-l clearfix">

    <h3>Blackbox logos and icons:</h3>

    <div class="partner-banner-download first">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/bbr-logo-blackonwhite-linear-thumb.png')}"
             alt=""/>

        <div class="embed">
            <h4>BLACK 3D LINEAR <span>(810x116)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=bbr-logo-blackonwhite-linear.png" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>

        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/bbr-logo-blackonwhite-linear.png')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

    <div class="partner-banner-download">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/bbr-logo-whiteonblack-linear-thumb.png')}"
             alt=""/>

        <div class="embed">
            <h4>WHITE 3D LINEAR <span>(810x116)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=bbr-logo-whiteonblack-linear.png" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>

        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/bbr-logo-whiteonblack-linear.png')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

    <div class="partner-banner-download first">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/bbr-logo-blackonwhite-stack-thumb.png')}" alt=""/>

        <div class="embed">
            <h4>BLACK 3D STACKED <span>(300x250)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=bbr-logo-blackonwhite-stack.png" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>

        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/bbr-logo-blackonwhite-stack.png')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

    <div class="partner-banner-download">
        <img class="asset-preview"
             src="${bb:libraryResource('/library/images/partner/bbr-logo-whiteonblack-stack-thumb.png')}" alt=""/>

        <div class="embed">
            <h4>WHITE 3D STACKED <span>(180x150)</span></h4>
            <textarea><a href="http://www.blackboxrepublic.com/partner/${actionBean.currentUser.username}" target="_blank"><img src="http://ads.blackboxrepublic.com/deliver.php?partner=${actionBean.currentUser.username}&ad=bbr-logo-whiteonblack-stack.png" alt="Join the Republic"/></a></textarea>
            <%--
                                           <button class="banner-clipboard"><img src="${bb:libraryResource('/library/images/partner/copy-to-clipboard-button.png')}" alt="Copy to Clipboard"/></button>
            --%>
        </div>

        <div class="asset-full-size"><a
                href="${bb:libraryResource('/library/images/partner/bbr-logo-whiteonblack-stack.png')}"><img
                src="${bb:libraryResource('/library/images/partner/icon-asset-full-size.gif')}"
                alt="View Full Size"/></a></div>
    </div>

</div>


</div>


</div>


</div>
</div>
</body>
</html>
