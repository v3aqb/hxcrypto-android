# Copyright (c) 2017-2018 v3aqb

# This file is part of hxcrypto.

# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or (at your option) any later version.
#
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
# 02110-1301  USA


class IVError(ValueError):
    pass


class IVChecker:
    # check reused iv, removing out-dated data automatically

    def __init__(self, maxlen=5000, timeout=3600):
        # create a IVStore for each key
        pass

    def check(self, key, iv):
        pass
