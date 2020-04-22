package cz.gregetom.graphwiki.commons.web.util

import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.net.InetAddress

object IpAddressProvider {

    /**
     * Provides ip address from http request.
     *
     * @return client ip address
     */
    fun provideClientIpAddress(): InetAddress {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        var ip: String = request.getHeader("X-FORWARDED-FOR") ?: request.remoteAddr
        if (ip == "0:0:0:0:0:0:0:1") ip = "127.0.0.1"
        require(ip.chars().filter { `$`: Int -> `$` == '.'.toInt() }.count() == 3L) { "Illegal IP: $ip" }
        return InetAddress.getByName(ip)
    }
}
